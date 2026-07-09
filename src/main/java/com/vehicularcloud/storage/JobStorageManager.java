package com.vehicularcloud.storage;

import com.vehicularcloud.model.Job;
import java.io.*;

/**
 * Manages storage of Job data to jobs.csv file
 * Stores jobs in format: jobID,jobName,duration,arrivalTime
 */
public class JobStorageManager {
    
    private static final String JOBS_FILE = "jobs.csv";
    
    /**
     * Saves a job to the jobs.csv file
     * @param jobID the ID of the job
     * @param jobName the name of the job
     * @param durationSeconds the duration of the job in seconds
     * @return true if successful, false otherwise
     */
    public boolean saveJob(int jobID, String jobName, int durationSeconds) {
        try {
            long arrivalTime = System.currentTimeMillis();
            Job job = new Job(jobID, jobName, durationSeconds, arrivalTime);
            
            try (FileWriter writer = new FileWriter(JOBS_FILE, true)) {
                writer.write(job.toString() + "\n");
            }
            
            System.out.println("Job saved successfully: ID=" + jobID + ", Name=" + jobName + ", Duration=" + durationSeconds + " seconds");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving job: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if the jobs file exists
     * @return true if file exists
     */
    public boolean fileExists() {
        return new File(JOBS_FILE).exists();
    }
    
    /**
     * Gets the jobs file name
     * @return the file name
     */
    public String getFileName() {
        return JOBS_FILE;
    }
}

