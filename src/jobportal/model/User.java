package jobportal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jobportal.DBConnection;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String location;
    private String skills;

    // Constructor for new user
    public User(String name, String email, String password, String location, String skills) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.skills = skills;
    }

    // Constructor for existing user
    public User(int userId, String name, String email, String password, String location, String skills) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.skills = skills;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    // Database operations
    // Create a new user
    public boolean save() {
        String sql = "INSERT INTO Users (name, email, password, location, skills) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, skills);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto-generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.userId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        } finally {
            // Close statement (connection will remain open for the application's lifecycle)
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Update existing user
    public boolean update() {
        String sql = "UPDATE Users SET name = ?, email = ?, password = ?, location = ?, skills = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, skills);
            stmt.setInt(6, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            // Close statement
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Delete user
    public boolean delete() {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        } finally {
            // Close statement
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
        return false;
    }

    // Find user by ID
    public static User findById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location"),
                        rs.getString("skills")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        } finally {
            // Close result set and statement
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    // Find user by email
    public static User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location"),
                        rs.getString("skills")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        } finally {
            // Close result set and statement
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    // Find users by skills
    public static List<User> findBySkills(String skillsToMatch) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE skills LIKE ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, "%" + skillsToMatch + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location"),
                        rs.getString("skills")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding users by skills: " + e.getMessage());
        } finally {
            // Close result set and statement
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return users;
    }

    // Get all users
    public static List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location"),
                        rs.getString("skills")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        } finally {
            // Close result set and statement
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return users;
    }

    // Authenticate user
    public static User authenticate(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location"),
                        rs.getString("skills")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        } finally {
            // Close result set and statement
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}