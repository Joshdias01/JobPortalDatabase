package jobportal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jobportal.DBConnection;

public class Application {
    private int applicationId;
    private int jobId;
    private int userId;
    private Date applicationDate;
    private String status;

    // Constructor for new application
    public Application(int jobId, int userId, Date applicationDate, String status) {
        this.jobId = jobId;
        this.userId = userId;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    // Constructor for existing application
    public Application(int applicationId, int jobId, int userId, Date applicationDate, String status) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.userId = userId;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    // Getters and setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Date applicationDate) { this.applicationDate = applicationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Database operations
    // Create a new application
    public boolean save() {
        String sql = "INSERT INTO Applications (job_id, user_id, application_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, jobId);
            stmt.setInt(2, userId);
            stmt.setDate(3, applicationDate);
            stmt.setString(4, status);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.applicationId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving application: " + e.getMessage());
        }
        return false;
    }

    // Update existing application
    public boolean update() {
        String sql = "UPDATE Applications SET job_id = ?, user_id = ?, application_date = ?, status = ? WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            stmt.setInt(2, userId);
            stmt.setDate(3, applicationDate);
            stmt.setString(4, status);
            stmt.setInt(5, applicationId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating application: " + e.getMessage());
        }
        return false;
    }

    // Delete application
    public boolean delete() {
        String sql = "DELETE FROM Applications WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting application: " + e.getMessage());
        }
        return false;
    }

    // Find application by ID
    public static Application findById(int applicationId) {
        String sql = "SELECT * FROM Applications WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getDate("application_date"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding application: " + e.getMessage());
        }
        return null;
    }

    // Find applications by job ID
    public static List<Application> findByJobId(int jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM Applications WHERE job_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applications.add(new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getDate("application_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding applications by job ID: " + e.getMessage());
        }
        return applications;
    }

    // Find applications by user ID
    public static List<Application> findByUserId(int userId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM Applications WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applications.add(new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getDate("application_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding applications by user ID: " + e.getMessage());
        }
        return applications;
    }

    // Get all applications
    public static List<Application> findAll() {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM Applications";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                applications.add(new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getDate("application_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving applications: " + e.getMessage());
        }
        return applications;
    }

    // Get job for this application
    public JobPosting getJob() {
        return JobPosting.findById(this.jobId);
    }

    // Get user for this application
    public User getUser() {
        return User.findById(this.userId);
    }

    // Get interview for this application
    public Interview getInterview() {
        return Interview.findByApplicationId(this.applicationId);
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", jobId=" + jobId +
                ", userId=" + userId +
                ", applicationDate=" + applicationDate +
                ", status='" + status + '\'' +
                '}';
    }
}