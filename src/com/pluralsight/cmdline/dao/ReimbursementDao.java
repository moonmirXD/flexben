package com.pluralsight.cmdline.dao;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.MySqlConnection;
import com.pluralsight.cmdline.bean.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class ReimbursementDao {
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<FlexCutOff> cutoffs = new ArrayList<>();
    ArrayList<Reimbursement> reimbursements = new ArrayList<>();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDateTime now = LocalDateTime.now();

    public ArrayList<Category> getCategory(){
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM category");

            while(rs.next()) {
                categories.add(
                    new Category(
                    rs.getInt("category_id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("added_by")
                    ));
            }
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
        return categories;
    }

    public void createReimbursement(HashMap<String, String> reimbursement){
        try {
            Connection connection = MySqlConnection.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO flex_reimbursement (employee_id, flex_cut_off_id, total_reimbursement_amount, date_submitted, status) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, Integer.parseInt(reimbursement.get("Employee ID")));
                stmt.setInt(2, Integer.parseInt(reimbursement.get("Flex Cutoff ID")));
                stmt.setDouble(3, Double.parseDouble(reimbursement.get("Flex Cutoff Amount")));
                stmt.setString(4, dtf.format(now));
                stmt.setString(5, "draft"); // default status;
                int affectedRows = stmt.executeUpdate();
                connection.commit();

                if (affectedRows == 0) {
                    throw new SQLException("Creating flex reimbursement failed, no rows affected.");
                }
                System.out.println("Reimbursement is successfully inserted: "+ reimbursement);
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println("Reimbursement Detail ID: " + generatedKeys.getLong(1) + " " +
                                "Successfully inserted!");
                        try (PreparedStatement statement = connection
                                .prepareStatement("INSERT INTO " +
                                        "flex_reimbursement_detail" +
                                        "(flex_reimbursement_id, or_number, " +
                                        "name_of_establishment, tin_of_establishment, amount, category_id, status, " +
                                        "date_added) VALUES " +
                                        "(?, ?, ?, ?, ?, ?, ?, ?)")) {
                            statement.setLong(1, generatedKeys.getLong(1));
                            statement.setInt(2, Integer.parseInt(reimbursement.get("OR Number")));
                            statement.setString(3, reimbursement.get("Name of Establishment"));
                            statement.setString(4, reimbursement.get("TIN of Establishment"));
                            statement.setDouble(5, Double.parseDouble(reimbursement.get("Amount")));
                            statement.setInt(6, Integer.parseInt(reimbursement.get("Category ID")));
                            statement.setString(7, "draft");
                            statement.setString(8, dtf.format(now));

                            statement.executeUpdate();
                            connection.commit();
                        }
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReimbursement(int id){
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            int affectedRows = stmt.executeUpdate("DELETE FROM flex_reimbursement_detail WHERE flex_reimbursement_id " +
                    "= " + id);
            stmt.executeUpdate("DELETE FROM flex_reimbursement WHERE flex_reimbursement_id = " + id);

            if (affectedRows == 0) {
                throw new SQLException("Deleting flex reimbursement failed, no rows affected.");
            }

            System.out.println("Successfully deleted!");
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }

    public void updateReimbursement(int id) {
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            stmt.executeUpdate("UPDATE flex_reimbursement_detail SET status = 'submitted' WHERE flex_reimbursement_id =" +
                    " " + id);
            stmt.executeUpdate("UPDATE flex_reimbursement SET status = 'submitted' WHERE flex_reimbursement_id = " + id);
            System.out.println("Successfully updated!");
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }


    public List<Reimbursement> getReimbursements(){
        try {
            Account user = Authentication.getUser();

            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM flex_reimbursement INNER JOIN " +
                    "flex_reimbursement_detail ON flex_reimbursement.flex_reimbursement_id = " +
                    "flex_reimbursement_detail.flex_reimbursement_detail_id WHERE employee_id = " + user.getEmployeeId());
            while(rs.next()) {
                reimbursements.add(
                        new Reimbursement(
                                rs.getInt("flex_reimbursement_id"),
                                rs.getInt("employee_id"),
                                rs.getInt("flex_cut_off_id"),
                                rs.getInt("transaction_number"),
                                rs.getDouble("total_reimbursement_amount"),
                                rs.getDate("date_submitted"),
                                rs.getString("status"),
                                rs.getInt("flex_reimbursement_detail_id"),
                                rs.getInt("or_number"),
                                rs.getInt("category_id"),
                                rs.getString("name_of_establishment"),
                                rs.getString("tin_of_establishment"),
                                rs.getDouble("amount"),
                                rs.getDate("date_added")
                        ));
            }
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
        return reimbursements;
    }

    public Optional<FlexCutOff> getCutOff(){
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM flex_cycle_cutoff");

            while(rs.next()) {
                cutoffs.add(
                        new FlexCutOff(
                                rs.getInt("flex_cutoff_id"),
                                rs.getInt("flex_cycle_id"),
                                rs.getDate("start_date"),
                                rs.getDate("end_date"),
                                rs.getString("is_active").charAt(0),
                                rs.getDouble("cut_off_cap_amount"),
                                rs.getString("cut_off_description")
                        ));
            }
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
        return cutoffs.stream().filter(cutoff -> cutoff.getIsActive() == 'y').findFirst();
    }

}
