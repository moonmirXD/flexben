package com.pluralsight.cmdline.dao;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.MySqlConnection;
import com.pluralsight.cmdline.bean.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        boolean hasReimbursement = false;
        int reimbursementId = 0;
        ResultSet generatedKey = null;
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
                stmt.setString(5, "draft");


                // Add checker if there's an id already
                PreparedStatement ps =
                        connection.prepareStatement("SELECT * FROM flex_reimbursement WHERE employee_id = ? AND " +
                                "status = 'draft'");
                ps.setInt (1,Integer.parseInt(reimbursement.get("Employee ID")));
                ResultSet rs = ps.executeQuery();


                if (rs.next()) {
                    hasReimbursement = true;
                    reimbursementId = rs.getInt("flex_reimbursement_id");
                    System.out.println("reimbursementId " + reimbursementId);
                } else {
                    int affectedRows = stmt.executeUpdate();
                    connection.commit();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating flex reimbursement failed, no rows affected.");
                    }

                    System.out.println("Reimbursement is successfully inserted: "+ reimbursement);
                    generatedKey = stmt.getGeneratedKeys();
                }

                try {
                        try (PreparedStatement statement = connection
                                .prepareStatement("INSERT INTO " +
                                        "flex_reimbursement_detail" +
                                        "(flex_reimbursement_id, or_number, " +
                                        "name_of_establishment, tin_of_establishment, amount, category_id, status, " +
                                        "date_added) VALUES " +
                                        "(?, ?, ?, ?, ?, ?, ?, ?)")) {
                            if(hasReimbursement){
                                statement.setLong(1, reimbursementId);
                            } else {
                                generatedKey.next();
                                statement.setLong(1, generatedKey.getLong(1));
                            }
                            statement.setInt(2, Integer.parseInt(reimbursement.get("OR Number")));
                            statement.setString(3, reimbursement.get("Name of Establishment"));
                            statement.setString(4, reimbursement.get("TIN of Establishment"));
                            statement.setDouble(5, Double.parseDouble(reimbursement.get("Amount")));
                            statement.setInt(6, Integer.parseInt(reimbursement.get("Category ID")));
                            statement.setString(7, "draft");
                            statement.setString(8, reimbursement.get("Date"));

                            int affRows = statement.executeUpdate();
                            if (affRows == 0) {
                                throw new SQLException("Creating flex reimbursement details failed, no rows affected." +
                                        " Invalid" +
                                        " inputs.");
                            }
                            System.out.println("Reimbursement details is successfully inserted: "+ reimbursement);
                            connection.commit();
                        }
                }catch (Exception e){
                    System.out.println(e);
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
            int affectedRows = stmt.executeUpdate("DELETE FROM flex_reimbursement_detail WHERE " +
                    "flex_reimbursement_detail_id " +
                    "= " + id);

            if (affectedRows == 0) {
                throw new SQLException("Deleting flex reimbursement detail failed, no rows affected.");
            }

            System.out.println("Successfully deleted!");
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }

    public void updateTotalReimbursementAmount(double total){
        Account user = Authentication.getUser();
        try {
            Connection connection = MySqlConnection.getConnection();
            String query = "UPDATE flex_reimbursement set total_reimbursement_amount = ? WHERE employee_id = ? AND " +
                    "status = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setDouble   (1, total);
            preparedStmt.setInt(2, user.getEmployeeId());
            preparedStmt.setString(3, "draft");
            preparedStmt.executeUpdate();

            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }

    public void updateReimbursement(int employeeId, String transactionNumber) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Optional<Reimbursement> reimbursement = getReimbursements().stream().filter(item -> item.getEmployeeId() == employeeId && item.getStatus().equalsIgnoreCase(
                        "draft")).findFirst();

        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            stmt.executeUpdate("UPDATE flex_reimbursement_detail SET status = 'submitted' WHERE " +
                    "flex_reimbursement_id = " + reimbursement.get().getFlexReimbursementId());

            String sql = "UPDATE flex_reimbursement SET status = ?, transaction_number = ?, date_submitted = ? WHERE " +
                    "employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "submitted");
            preparedStatement.setString(2, transactionNumber);
            preparedStatement.setString(3, dateFormat.format(date));
            preparedStatement.setInt(4, employeeId);
            preparedStatement.executeUpdate();

            System.out.println("Successfully updated!");
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }


    public List<Reimbursement> getReimbursements(){
        String sql = "SELECT * FROM flexben.flex_reimbursement_detail INNER JOIN flex_reimbursement ON flex_reimbursement_detail.flex_reimbursement_id = flex_reimbursement.flex_reimbursement_id";

        try {
            Account user = Authentication.getUser();

            if(user.getRoleName().equalsIgnoreCase("Employee")) {
                sql += " WHERE employee_id = " + user.getEmployeeId();
            }

            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()) {
                reimbursements.add(
                        new Reimbursement(
                                rs.getInt("flex_reimbursement_id"),
                                rs.getInt("employee_id"),
                                rs.getInt("flex_cut_off_id"),
                                rs.getString("transaction_number"),
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
