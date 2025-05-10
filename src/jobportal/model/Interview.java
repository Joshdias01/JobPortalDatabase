package jobportal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jobportal.DBConnection;

public class Interview {
    private int interviewId;
    private int applicationId;
    private Date scheduledDate;
    private String status;
    private String feedback;

    // Constructor for new interview
    public Interview(int applicationId, Date scheduledDate, String status, String feedback) {
        this.applicationId = applicationId;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.feedback = feedback;
    }

    // Constructor for existing interview
    public Interview(int interviewId, int applicationId, Date scheduledDate, String status, String feedback) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.feedback = feedback;
    }

    // Getters and setters
    public int getInterviewId() { return interviewId; }
    public void setInterviewId(int interviewId) { this.interviewId = interviewId; }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public Date getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    // Database operations
    // Create a new interview
    public boolean save() {
        String sql = "INSERT INTO Interviews (application_id, scheduled_date, status, feedback) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, applicationId);
            stmt.setDate(2, scheduledDate);
            stmt.setString(3, status);
            stmt.setString(4, feedback);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.interviewId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving interview: " + e.getMessage());
        }
        return false;
    }

    // Update existing interview
    public boolean update() {
        String sql = "UPDATE Interviews SET application_id = ?, scheduled_date = ?, status = ?, feedback = ? WHERE interview_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);
            stmt.setDate(2, scheduledDate);
            stmt.setString(3, status);
            stmt.setString(4, feedback);
            stmt.setInt(5, interviewId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating interview: " + e.getMessage());
        }
        return false;
    }

    // Delete interview
    public boolean delete() {
        String sql = "DELETE FROM Interviews WHERE interview_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, interviewId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting interview: " + e.getMessage());
        }
        return false;
    }

    // Find interview by ID
    public static Interview findById(int interviewId) {
        String sql = "SELECT * FROM Interviews WHERE interview_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, interviewId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Interview(
                        rs.getInt("interview_id"),
                        rs.getInt("application_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("status"),
                        rs.getString("feedback")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding interview: " + e.getMessage());
        }
        return null;
    }

    // Find interview by application ID
    public static Interview findByApplicationId(int applicationId) {
        String sql = "SELECT * FROM Interviews WHERE application_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Interview(
                        rs.getInt("interview_id"),
                        rs.getInt("application_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("status"),
                        rs.getString("feedback")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding interview by application ID: " + e.getMessage());
        }
        return null;
    }

    // Find interviews by status
    public static List<Interview> findByStatus(String status) {
        List<Interview> interviews = new ArrayList<>();
        String sql = "SELECT * FROM Interviews WHERE status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                interviews.add(new Interview(
                        rs.getInt("interview_id"),
                        rs.getInt("application_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("status"),
                        rs.getString("feedback")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding interviews by status: " + e.getMessage());
        }
        return interviews;
    }

    // Get all interviews
    public static List<Interview> findAll() {
        List<Interview> interviews = new ArrayList<>();
        String sql = "SELECT * FROM Interviews";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                interviews.add(new Interview(
                        rs.getInt("interview_id"),
                        rs.getInt("application_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("status"),
                        rs.getString("feedback")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving interviews: " + e.getMessage());
        }
        return interviews;
    }

    // Find interviews by date range
    public static List<Interview> findByDateRange(Date startDate, Date endDate) {
        List<Interview> interviews = new ArrayList<>();
        String sql = "SELECT * FROM Interviews WHERE scheduled_date BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                interviews.add(new Interview(
                        rs.getInt("interview_id"),
                        rs.getInt("application_id"),
                        rs.getDate("scheduled_date"),
                        rs.getString("status"),
                        rs.getString("feedback")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding interviews by date range: " + e.getMessage());
        }
        return interviews;
    }

    // Get application for this interview
    public Application getApplication() {
        return Application.findById(this.applicationId);
    }

    // Get job details for this interview through the application
    public JobPosting getJob() {
        Application app = getApplication();
        return app != null ? app.getJob() : null;
    }

    // Get user details for this interview through the application
    public User getUser() {
        Application app = getApplication();
        return app != null ? app.getUser() : null;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "interviewId=" + interviewId +
                ", applicationId=" + applicationId +
                ", scheduledDate=" + scheduledDate +
                ", status='" + status + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}