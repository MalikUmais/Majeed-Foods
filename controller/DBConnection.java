package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    private DBConnection() { }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
           
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://DESKTOP-21F9PGV:1433;"
                    + "databaseName=Majeed_Foods; "
                    + "integratedSecurity=true; "
                    + "encrypt=false; "
                    + "trustServerCertificate=true;";

            connection = DriverManager.getConnection(url);
            System.out.println("✔ Connected to SQL Server Successfully.");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQL Server JDBC Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
        }

        return connection;
    }
}
