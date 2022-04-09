package com.pluralsight.cmdline.dao;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.MySqlConnection;
import com.pluralsight.cmdline.bean.Account;
import com.pluralsight.cmdline.bean.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AuthenticationDao {
    ArrayList<Account> accounts = new ArrayList<Account>();
    static Company company = new Company();

    {
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM account INNER JOIN employee ON account.employee_id = employee.employee_id INNER JOIN role ON employee.role_id = role.role_id");

            while(rs.next()) {
                accounts.add(new Account(
                        rs.getInt("account_id"),
                        rs.getInt("employee_id"),
                        rs.getString("password"),
                        rs.getString("is_active").charAt(0),
                        rs.getInt("employee_number"),
                        rs.getInt("company_id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getInt("role_id"),
                        rs.getString("name"),
                        rs.getString("description")
                        ));
            }
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }

    public static Company getCompany(int companyId){
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt= connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM company WHERE company_id = " + companyId);

            while(rs.next()) {
                company = new Company(
                        rs.getInt("company_id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
            connection.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
        return company;
    }

    public AuthenticationDao(){}

    public ArrayList<Account> getAccounts(){
        return accounts;
    }
}
