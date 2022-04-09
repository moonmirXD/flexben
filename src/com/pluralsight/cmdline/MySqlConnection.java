package com.pluralsight.cmdline;
import java.sql.*;

public class MySqlConnection {
    final static String database = "flexben";
    final static String connectionURL = "jdbc:mysql://localhost:3306/" + database + "?characterEncoding=latin1&useConfigs=maxPerformance";
    final static String userDB = "root";
    final static String passwordDB = "root";

    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(connectionURL, userDB, passwordDB);
            System.out.println("Connected to Database!");
            con.close();
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(connectionURL, userDB, passwordDB);
            return con;
        } catch (Exception e) {
            ErrorHandler.throwError(String.valueOf(e), 502);
        }
        return null;
    }
}
