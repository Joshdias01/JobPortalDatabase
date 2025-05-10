import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/job_portal";
        String user = "root";
        String password = "Joshdias1234";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Oops! Connection failed.");
            e.printStackTrace();
        }
    }
}
