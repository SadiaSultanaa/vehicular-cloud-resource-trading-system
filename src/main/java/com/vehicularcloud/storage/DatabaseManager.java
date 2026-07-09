package com.vehicularcloud.storage;

import com.vehicularcloud.model.Job;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Manager for VCRTS MySQL integration.
 * Member 3: Database Design & JDBC Integration
 * Milestone 6
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/vcrts_db";
    private static final String USER = "root";
    private static final String PASS = System.getenv("MYSQL_PASSWORD") != null
            ? System.getenv("MYSQL_PASSWORD")
            : "";

    private static DatabaseManager instance;

    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DB] MySQL Driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] MySQL Driver not found");
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean insertJob(String clientId, String jobName, double duration, String deadline) {
        String sql = "INSERT INTO jobs (client_id, job_name, duration, deadline) VALUES ('"
                + clientId + "', '"
                + jobName + "', "
                + duration + ", '"
                + deadline + "')";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("[DB] Job inserted: " + jobName);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error inserting job: " + e.getMessage());
        }
        return false;
    }

    public boolean insertVehicleOwner(String ownerId, String make, String model,
            int year, String plate, double hours) {
        String sql = "INSERT INTO vehicle_owners (owner_id, vehicle_make, vehicle_model, vehicle_year, license_plate, residency_hours) VALUES ('"
                + ownerId + "', '"
                + make + "', '"
                + model + "', "
                + year + ", '"
                + plate + "', "
                + hours + ")";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("[DB] Vehicle owner inserted: " + ownerId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error inserting vehicle owner: " + e.getMessage());
        }
        return false;
    }

    public void printAllJobs() {
        String sql = "SELECT * FROM jobs";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Jobs ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Client: " + rs.getString("client_id") +
                        ", Job: " + rs.getString("job_name") +
                        ", Created: " + rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void printAllVehicleOwners() {
        String sql = "SELECT * FROM vehicle_owners";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Vehicle Owners ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Owner: " + rs.getString("owner_id") +
                        ", Vehicle: " + rs.getString("vehicle_make") + " " + rs.getString("vehicle_model") +
                        ", Created: " + rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves all jobs from the database for completion time calculation.
     * Converts duration from hours to seconds and created_at to milliseconds.
     * 
     * @return List of Job objects ready for completion time calculation
     * @throws SQLException if database query fails
     */
    public List<Job> getAllJobsForCompletion() throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT id, job_name, duration, created_at FROM jobs ORDER BY created_at ASC";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int jobID = rs.getInt("id");
                String jobName = rs.getString("job_name");

                // Convert duration from hours to seconds
                double durationHours = rs.getDouble("duration");
                int durationSeconds = (int) (durationHours * 3600);

                // Convert created_at timestamp to milliseconds
                Timestamp createdAt = rs.getTimestamp("created_at");
                long arrivalTime = createdAt != null ? createdAt.getTime() : System.currentTimeMillis();

                jobs.add(new Job(jobID, jobName, durationSeconds, arrivalTime));
            }
        }

        return jobs;
    }
}
