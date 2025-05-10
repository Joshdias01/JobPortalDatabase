package jobportal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jobportal.DBConnection;

public class JobPosting {
    private int jobId;
    private int companyId;
    private String title;
    private String description;
    private String location;
    private String skillsRequired;
    private Date datePosted;

    // Constructor for new job posting
    public JobPosting(int companyId, String title, String description, String location, String skillsRequired, Date datePosted) {
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.skillsRequired = skillsRequired;
        this.datePosted = datePosted;
    }

    // Constructor for existing job posting
    public JobPosting(int jobId, int companyId, String title, String description, String location, String skillsRequired, Date datePosted) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.skillsRequired = skillsRequired;
        this.datePosted = datePosted;
    }

    // Getters and setters
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }

    public Date getDatePosted() { return datePosted; }
    public void setDatePosted(Date datePosted) { this.datePosted = datePosted; }

    // Database operations
    // Create a new job posting
    public boolean save() {
        String sql = "INSERT INTO Job_Postings (company_id, title, description, location, skills_required, date_posted) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, companyId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setString(5, skillsRequired);
            stmt.setDate(6, datePosted);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.jobId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving job posting: " + e.getMessage());
        }
        return false;
    }

    // Update existing job posting
    public boolean update() {
        String sql = "UPDATE Job_Postings SET company_id = ?, title = ?, description = ?, location = ?, skills_required = ?, date_posted = ? WHERE job_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setString(5, skillsRequired);
            stmt.setDate(6, datePosted);
            stmt.setInt(7, jobId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job posting: " + e.getMessage());
        }
        return false;
    }

    // Delete job posting
    public boolean delete() {
        String sql = "DELETE FROM Job_Postings WHERE job_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting job posting: " + e.getMessage());
        }
        return false;
    }

    // Find job posting by ID
    public static JobPosting findById(int jobId) {
        String sql = "SELECT * FROM Job_Postings WHERE job_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new JobPosting(
                        rs.getInt("job_id"),
                        rs.getInt("company_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("skills_required"),
                        rs.getDate("date_posted")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding job posting: " + e.getMessage());
        }
        return null;
    }

    // Find job postings by company ID
    public static List<JobPosting> findByCompanyId(int companyId) {
        List<JobPosting> jobPostings = new ArrayList<>();
        String sql = "SELECT * FROM Job_Postings WHERE company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jobPostings.add(new JobPosting(
                        rs.getInt("job_id"),
                        rs.getInt("company_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("skills_required"),
                        rs.getDate("date_posted")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding job postings by company: " + e.getMessage());
        }
        return jobPostings;
    }

    // Search job postings by skills
    public static List<JobPosting> searchBySkills(String skills) {
        List<JobPosting> jobPostings = new ArrayList<>();
        String sql = "SELECT * FROM Job_Postings WHERE skills_required LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + skills + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jobPostings.add(new JobPosting(
                        rs.getInt("job_id"),
                        rs.getInt("company_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("skills_required"),
                        rs.getDate("date_posted")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching job postings by skills: " + e.getMessage());
        }
        return jobPostings;
    }

    // Search job postings by location
    public static List<JobPosting> searchByLocation(String location) {
        List<JobPosting> jobPostings = new ArrayList<>();
        String sql = "SELECT * FROM Job_Postings WHERE location LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jobPostings.add(new JobPosting(
                        rs.getInt("job_id"),
                        rs.getInt("company_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("skills_required"),
                        rs.getDate("date_posted")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching job postings by location: " + e.getMessage());
        }
        return jobPostings;
    }

    // Get all job postings
    public static List<JobPosting> findAll() {
        List<JobPosting> jobPostings = new ArrayList<>();
        String sql = "SELECT * FROM Job_Postings";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jobPostings.add(new JobPosting(
                        rs.getInt("job_id"),
                        rs.getInt("company_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("skills_required"),
                        rs.getDate("date_posted")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving job postings: " + e.getMessage());
        }
        return jobPostings;
    }

    // Get applications for this job
    public List<Application> getApplications() {
        return Application.findByJobId(this.jobId);
    }

    // Get company for this job
    public Company getCompany() {
        return Company.findById(this.companyId);
    }

    @Override
    public String toString() {
        return "JobPosting{" +
                "jobId=" + jobId +
                ", companyId=" + companyId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", skillsRequired='" + skillsRequired + '\'' +
                ", datePosted=" + datePosted +
                '}';
    }
}