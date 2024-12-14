package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ucss"; // localhost will be replaced with Docker hostname
    private static final String USER = "root"; // it will be replaced with DB username
    private static final String PASSWORD = "your_password"; // it will be replaced with DB password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
