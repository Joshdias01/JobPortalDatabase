package jobportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for the Job Portal application.
 * Manages database connections with proper pooling and error handling.
 */
public class DBConnection {
    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal";
    private static final String USER = "root";
    private static final String PASSWORD = "Joshdias1234"; // Change to your actual password

    // JDBC driver
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Connection pool - in a real application, you might use a connection pool library
    private static Connection connection = null;

    static {
        // Load the JDBC driver
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver: " + e.getMessage());
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    /**
     * Get a database connection.
     * This method will create a new connection if one doesn't exist or if the existing one is closed.
     *
     * @return A valid database connection
     */
    public static Connection getConnection() {
        try {
            // Check if connection is null or closed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Close the database connection if it exists and is open.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Create a fresh connection for a new transaction.
     * This can be used when you need a separate connection with its own transaction scope.
     *
     * @return A new database connection
     */
    public static Connection createFreshConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error creating fresh database connection: " + e.getMessage());
            throw new RuntimeException("Failed to create fresh connection", e);
        }
    }
}