// JobPortalApp.java
package jobportal;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import jobportal.model.*;

public class JobPortalApp {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("Welcome to the Job Portal System!");

        // Initialize database connection
        DBConnection.getConnection();

        boolean exit = false;
        while (!exit) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }

            int choice = getUserChoice();

            if (currentUser == null) {
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                switch (choice) {
                    case 1:
                        viewProfile();
                        break;
                    case 2:
                        updateProfile();
                        break;
                    case 3:
                        searchJobs();
                        break;
                    case 4:
                        applyForJob();
                        break;
                    case 5:
                        viewApplications();
                        break;
                    case 6:
                        viewInterviews();
                        break;
                    case 7:
                        logout();
                        break;
                    case 8:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        // Close database connection
        DBConnection.closeConnection();
        System.out.println("Thank you for using Job Portal System. Goodbye!");
    }

    private static void showLoginMenu() {
        System.out.println("\n===== Login Menu =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void showMainMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("Welcome, " + currentUser.getName() + "!");
        System.out.println("1. View Profile");
        System.out.println("2. Update Profile");
        System.out.println("3. Search Jobs");
        System.out.println("4. Apply for a Job");
        System.out.println("5. View My Applications");
        System.out.println("6. View My Interviews");
        System.out.println("7. Logout");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void login() {
        System.out.println("\n===== Login =====");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = User.authenticate(email, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void register() {
        System.out.println("\n===== Register =====");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter skills (comma separated): ");
        String skills = scanner.nextLine();

        User existingUser = User.findByEmail(email);
        if (existingUser != null) {
            System.out.println("Email already registered. Please use a different email.");
            return;
        }

        User newUser = new User(name, email, password, location, skills);
        if (newUser.save()) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private static void viewProfile() {
        System.out.println("\n===== Your Profile =====");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Location: " + currentUser.getLocation());
        System.out.println("Skills: " + currentUser.getSkills());
    }

    private static void updateProfile() {
        System.out.println("\n===== Update Profile =====");
        System.out.print("Enter new name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            currentUser.setName(name);
        }

        System.out.print("Enter new location (or press Enter to keep current): ");
        String location = scanner.nextLine();
        if (!location.isEmpty()) {
            currentUser.setLocation(location);
        }

        System.out.print("Enter new skills (or press Enter to keep current): ");
        String skills = scanner.nextLine();
        if (!skills.isEmpty()) {
            currentUser.setSkills(skills);
        }

        System.out.print("Enter new password (or press Enter to keep current): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            currentUser.setPassword(password);
        }

        if (currentUser.update()) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile. Please try again.");
        }
    }

    private static void searchJobs() {
        System.out.println("\n===== Search Jobs =====");
        System.out.println("1. Search by Skills");
        System.out.println("2. Search by Location");
        System.out.println("3. View All Jobs");
        System.out.print("Enter your choice: ");

        int choice = getUserChoice();
        List<JobPosting> jobPostings = null;

        switch (choice) {
            case 1:
                System.out.print("Enter skills to search: ");
                String skills = scanner.nextLine();
                jobPostings = JobPosting.searchBySkills(skills);
                break;
            case 2:
                System.out.print("Enter location to search: ");
                String location = scanner.nextLine();
                jobPostings = JobPosting.searchByLocation(location);
                break;
            case 3:
                jobPostings = JobPosting.findAll();
                break;
            default:
                System.out.println("Invalid choice. Showing all jobs.");
                jobPostings = JobPosting.findAll();
        }

        if (jobPostings.isEmpty()) {
            System.out.println("No jobs found matching your criteria.");
        } else {
            System.out.println("\nFound " + jobPostings.size() + " job(s):");
            for (JobPosting job : jobPostings) {
                Company company = job.getCompany();
                System.out.println("Job ID: " + job.getJobId() +
                        " | Title: " + job.getTitle() +
                        " | Company: " + (company != null ? company.getName() : "Unknown") +
                        " | Location: " + job.getLocation());
            }
        }
    }

    private static void applyForJob() {
        System.out.println("\n===== Apply for a Job =====");
        System.out.print("Enter Job ID: ");
        try {
            int jobId = Integer.parseInt(scanner.nextLine());

            JobPosting job = JobPosting.findById(jobId);
            if (job == null) {
                System.out.println("Job not found with ID: " + jobId);
                return;
            }

            // Check if already applied
            List<Application> applications = Application.findByUserId(currentUser.getUserId());
            for (Application app : applications) {
                if (app.getJobId() == jobId) {
                    System.out.println("You have already applied for this job.");
                    return;
                }
            }

            // Create new application
            Application application = new Application(
                    jobId,
                    currentUser.getUserId(),
                    new Date(System.currentTimeMillis()),
                    "Pending"
            );

            if (application.save()) {
                System.out.println("Application submitted successfully!");
            } else {
                System.out.println("Failed to submit application. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Job ID. Please enter a valid number.");
        }
    }

    private static void viewApplications() {
        System.out.println("\n===== My Applications =====");
        List<Application> applications = Application.findByUserId(currentUser.getUserId());

        if (applications.isEmpty()) {
            System.out.println("You haven't applied for any jobs yet.");
        } else {
            for (Application app : applications) {
                JobPosting job = app.getJob();
                System.out.println("Application ID: " + app.getApplicationId() +
                        " | Job: " + (job != null ? job.getTitle() : "Unknown") +
                        " | Date: " + app.getApplicationDate() +
                        " | Status: " + app.getStatus());
            }
        }
    }

    private static void viewInterviews() {
        System.out.println("\n===== My Interviews =====");
        List<Application> applications = Application.findByUserId(currentUser.getUserId());
        boolean hasInterviews = false;

        for (Application app : applications) {
            Interview interview = app.getInterview();
            if (interview != null) {
                hasInterviews = true;
                JobPosting job = app.getJob();
                System.out.println("Interview ID: " + interview.getInterviewId() +
                        " | Job: " + (job != null ? job.getTitle() : "Unknown") +
                        " | Date: " + interview.getScheduledDate() +
                        " | Status: " + interview.getStatus());
            }
        }

        if (!hasInterviews) {
            System.out.println("You don't have any scheduled interviews.");
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}