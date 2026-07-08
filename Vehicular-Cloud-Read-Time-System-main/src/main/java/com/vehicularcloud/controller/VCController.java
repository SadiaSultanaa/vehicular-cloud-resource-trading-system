package com.vehicularcloud.controller;

import com.vehicularcloud.model.Job;
import com.vehicularcloud.storage.DatabaseManager;
import java.util.*;
import java.io.*;
import java.sql.SQLException;

/**
 * Controls the VC system and calculates job completion times.
 * Similar to Bank class in ATM example - handles business logic.
 * 
 * Project: VCRTS (Vehicle Cloud Real-Time System)
 * Name: Sadia
 */
public class VCController {
    private ArrayList<Job> jobs;

    /**
     * Constructs a VC controller with no jobs.
     */
    public VCController() {
        jobs = new ArrayList<Job>();
    }

    /**
     * Reads jobs from the database.
     * This is the primary method for reading jobs for completion time calculation.
     * 
     * @throws SQLException if database query fails
     */
    public void readJobsFromDatabase() throws SQLException {
        jobs.clear();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        List<Job> dbJobs = dbManager.getAllJobsForCompletion();
        jobs.addAll(dbJobs);
    }

    /**
     * Reads jobs from the jobs file.
     * Similar to Bank.readCustomers() in ATM example.
     * Kept for backward compatibility, but readJobsFromDatabase() is preferred.
     * 
     * @param filename the name of the jobs file
     * @throws IOException if file cannot be read
     */
    public void readJobs(String filename) throws IOException {
        jobs.clear();
        File file = new File(filename);

        if (!file.exists()) {
            return;
        }

        Scanner in = new Scanner(file);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.trim().isEmpty())
                continue; // Skip empty lines

            String[] parts = line.split(",");
            if (parts.length == 4) {
                // New format: jobID,jobName,duration,arrivalTime
                int jobID = Integer.parseInt(parts[0]);
                String jobName = parts[1];
                int duration = Integer.parseInt(parts[2]);
                long arrivalTime = Long.parseLong(parts[3]);
                jobs.add(new Job(jobID, jobName, duration, arrivalTime));
            } else if (parts.length == 3) {
                // Old format support: jobID,duration,arrivalTime
                int jobID = Integer.parseInt(parts[0]);
                int duration = Integer.parseInt(parts[1]);
                long arrivalTime = Long.parseLong(parts[2]);
                jobs.add(new Job(jobID, "Unnamed Job", duration, arrivalTime));
            }
        }
        in.close();
    }

    /**
     * Calculates completion times for all jobs using FIFO.
     * 
     * @return array of results [jobID, jobName, duration, completionTime]
     */
    public String[][] calculateCompletionTimes() {
        // Sort by arrival time (FIFO)
        Collections.sort(jobs, new Comparator<Job>() {
            public int compare(Job j1, Job j2) {
                return Long.compare(j1.getArrivalTime(), j2.getArrivalTime());
            }
        });

        String[][] results = new String[jobs.size()][4];
        int cumulativeTime = 0;

        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            cumulativeTime += job.getDuration();

            results[i][0] = String.valueOf(job.getJobID());
            results[i][1] = job.getJobName();
            results[i][2] = String.valueOf(job.getDuration());
            results[i][3] = String.valueOf(cumulativeTime);
        }

        return results;
    }

    /**
     * Gets the number of jobs.
     * 
     * @return number of jobs
     */
    public int getJobCount() {
        return jobs.size();
    }
}
