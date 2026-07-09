package com.vehicularcloud.storage;

import com.vehicularcloud.controller.RequestType;

/**
 * Adapter class that bridges the VC Controller Server with the database storage system.
 * Parses incoming request strings and saves them to MySQL database.
 *
 * Member 4: Rezwan - Integration & Storage
 * Milestone 6
 */
public class ServerStorageAdapter {

    // Database manager for MySQL operations
    private static final DatabaseManager dbManager = DatabaseManager.getInstance();3

    /**
     * Parses and saves data from the VC Controller server to the MySQL database.
     *
     * @param type The type of request (TASK_OWNER or VEHICLE_OWNER)
     * @param request The comma-separated request string
     * @return true if data was saved successfully, false otherwise
     */
    public static boolean saveData(RequestType type, String request) {
        try {
            String[] parts = request.split(",");

            if (type == RequestType.VEHICLE_OWNER) {
                return saveVehicleOwner(parts);
            } else if (type == RequestType.TASK_OWNER) {
                return saveTaskOwner(parts);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric values: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error saving data from request: " + request);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Saves vehicle owner data to the database.
     * Expected format: VEHICLE_OWNER,ownerId,make,model,year,plate,residency
     */
    private static boolean saveVehicleOwner(String[] parts) {
        if (parts.length != 7) {
            System.err.println("Invalid VEHICLE_OWNER request format. Expected 7 parts, got " + parts.length);
            return false;
        }

        String ownerId = parts[1].trim();
        String make = parts[2].trim();
        String model = parts[3].trim();
        int year = Integer.parseInt(parts[4].trim());
        String plate = parts[5].trim();
        double residency = Double.parseDouble(parts[6].trim());

        // Save to MySQL database
        boolean success = dbManager.insertVehicleOwner(ownerId, make, model, year, plate, residency);

        if (success) {
            System.out.println("Saved vehicle owner to database: " + ownerId);
        } else {
            System.err.println("Failed to save vehicle owner to database: " + ownerId);
        }

        return success;
    }

    /**
     * Saves task owner (job) data to the database.
     * Expected format: TASK_OWNER,clientId,jobName,duration,deadline
     */
    private static boolean saveTaskOwner(String[] parts) {
        if (parts.length != 5) {
            System.err.println("Invalid TASK_OWNER request format. Expected 5 parts, got " + parts.length);
            return false;
        }

        String clientId = parts[1].trim();
        String jobName = parts[2].trim();
        double duration = Double.parseDouble(parts[3].trim());
        String deadline = parts[4].trim();

        // Save to MySQL database
        boolean success = dbManager.insertJob(clientId, jobName, duration, deadline);

        if (success) {
            System.out.println("Saved job to database: " + clientId + " - " + jobName);
            
            // Also save to jobs.csv for completion time calculation
            try {
                JobStorageManager jobStorage = new JobStorageManager();
                
                // Convert duration from hours to seconds
                int durationSeconds = (int)(duration * 3600);
                
                // Generate job ID using hash of clientId + jobName for deterministic IDs
                int jobID = Math.abs((clientId + jobName).hashCode() % 1000000);
                
                // Save to jobs.csv
                boolean csvSuccess = jobStorage.saveJob(jobID, jobName, durationSeconds);
                
                if (csvSuccess) {
                    System.out.println("Saved job to jobs.csv: ID=" + jobID + ", Name=" + jobName);
                } else {
                    System.err.println("Warning: Failed to save job to jobs.csv, but database save succeeded");
                }
            } catch (Exception e) {
                System.err.println("Error saving job to jobs.csv: " + e.getMessage());
                e.printStackTrace();
                // Don't fail the entire operation if CSV save fails, database save succeeded
            }
        } else {
            System.err.println("Failed to save job to database: " + clientId);
        }

        return success;
    }
}
