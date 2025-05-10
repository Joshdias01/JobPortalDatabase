# **Job Portal System Project Description**

# 💼 Job Portal System

A **console-based Java application** that connects job seekers with employers. It simplifies the job search, application, and interview process through a centralized system.

---

## 🚀 Project Overview

The Job Portal System is a comprehensive, Java-based console application built using **JDBC** for MySQL database connectivity. It allows users to register, search jobs, apply, and manage interviews — all from the terminal.

---

## 🧩 Features

### 👤 User Management
- Register with personal details, skills, and location
- Secure login/authentication system
- View and update user profile

### 💼 Job Management
- View comprehensive job listings
- Search jobs by **skills**, **location**, or view all
- View job details, requirements, and company info

### 📑 Application Process
- Submit job applications
- Track status of submitted applications

### 🗓️ Interview Management
- Schedule interviews
- Track interview status (Scheduled / Completed / Cancelled)
- Add/view feedback for interviews

---

## 🛠️ Technologies Used

- **Java**
- **MySQL Database**
- **JDBC** for DB connectivity
- **Console-based UI**

---

## 🔧 Prerequisites

- JDK 11 or higher
- MySQL Server 8.0+
- (Optional) Maven for dependency management

---



# Job Portal System - Setup Instructions

This document explains how to set up and run the Job Portal System application.

## Prerequisites

1. Java Development Kit (JDK) 8 or higher
2. MySQL Server 5.7 or higher
3. MySQL Connector/J (JDBC driver)

## Database Setup

1. Create a database in MySQL:

```sql
CREATE DATABASE job_portal;
USE job_portal;
```

2. Create the required tables using the provided SQL queries:

```sql
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    location VARCHAR(100),
    skills TEXT
);

CREATE TABLE Companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE,
    location VARCHAR(100),
    industry VARCHAR(100)
);

CREATE TABLE Job_Postings (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    title VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    skills_required TEXT,
    date_posted DATE,
    FOREIGN KEY (company_id) REFERENCES Companies(company_id)
);

CREATE TABLE Applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    user_id INT,
    application_date DATE,
    status VARCHAR(50),
    FOREIGN KEY (job_id) REFERENCES Job_Postings(job_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Interviews (
    interview_id INT PRIMARY KEY AUTO_INCREMENT,
    application_id INT,
    scheduled_date DATE,
    status VARCHAR(50),
    feedback TEXT,
    FOREIGN KEY (application_id) REFERENCES Applications(application_id)
);
```

3. Insert some sample data (optional):

```sql
-- Insert sample companies
INSERT INTO Companies (name, location, industry) VALUES
('Tech Innovations Inc.', 'New York', 'Technology'),
('Global Finance Group', 'Chicago', 'Finance'),
('HealthCare Solutions', 'Boston', 'Healthcare');

-- Insert sample job postings
INSERT INTO Job_Postings (company_id, title, description, location, skills_required, date_posted) VALUES
(1, 'Java Developer', 'Developing enterprise applications using Java and related technologies.', 'New York', 'Java, Spring, SQL', '2025-05-01'),
(1, 'DevOps Engineer', 'Managing CI/CD pipelines and cloud infrastructure.', 'Remote', 'Docker, Kubernetes, AWS', '2025-05-02'),
(2, 'Financial Analyst', 'Analyzing financial data and creating reports.', 'Chicago', 'Excel, SQL, Financial Modeling', '2025-05-03'),
(3, 'Medical Data Scientist', 'Analyzing healthcare data to improve patient outcomes.', 'Boston', 'Python, R, Statistics, Healthcare', '2025-05-04');
```

## Project Setup

1. Create a new Java project in your preferred IDE (Eclipse, IntelliJ, etc.)

2. Create the following package structure:
    - `jobportal`
    - `jobportal.model`

3. Add MySQL Connector/J to your project's classpath. You can download it from:
   https://dev.mysql.com/downloads/connector/j/

4. Place the provided Java files in their respective packages:
    - In `jobportal` package:
        - `DBConnection.java`
        - `JobPortalApp.java`
    - In `jobportal.model` package:
        - `User.java`
        - `Company.java`
        - `JobPosting.java`
        - `Application.java`
        - `Interview.java`

5. Update the database connection details in `DBConnection.java` to match your MySQL setup:

```java
private static final String URL = "jdbc:mysql://localhost:3306/job_portal";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

## Running the Application

1. Compile the Java files
2. Run the `JobPortalApp` class
3. Follow the on-screen prompts to interact with the system

