package jobportal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jobportal.DBConnection;

public class Company {
    private int companyId;
    private String name;
    private String location;
    private String industry;

    // Constructor for new company
    public Company(String name, String location, String industry) {
        this.name = name;
        this.location = location;
        this.industry = industry;
    }

    // Constructor for existing company
    public Company(int companyId, String name, String location, String industry) {
        this.companyId = companyId;
        this.name = name;
        this.location = location;
        this.industry = industry;
    }

    // Getters and setters
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    // Database operations
    // Create a new company
    public boolean save() {
        String sql = "INSERT INTO Companies (name, location, industry) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, industry);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.companyId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving company: " + e.getMessage());
        }
        return false;
    }

    // Update existing company
    public boolean update() {
        String sql = "UPDATE Companies SET name = ?, location = ?, industry = ? WHERE company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, industry);
            stmt.setInt(4, companyId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating company: " + e.getMessage());
        }
        return false;
    }

    // Delete company
    public boolean delete() {
        String sql = "DELETE FROM Companies WHERE company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting company: " + e.getMessage());
        }
        return false;
    }

    // Find company by ID
    public static Company findById(int companyId) {
        String sql = "SELECT * FROM Companies WHERE company_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Company(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("industry")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding company: " + e.getMessage());
        }
        return null;
    }

    // Find company by name
    public static Company findByName(String name) {
        String sql = "SELECT * FROM Companies WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Company(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("industry")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding company by name: " + e.getMessage());
        }
        return null;
    }

    // Get all companies
    public static List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Companies";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                companies.add(new Company(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("industry")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving companies: " + e.getMessage());
        }
        return companies;
    }

    // Get job postings by company
    public List<JobPosting> getJobPostings() {
        return JobPosting.findByCompanyId(this.companyId);
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }
}