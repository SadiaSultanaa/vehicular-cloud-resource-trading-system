package com.vehicularcloud.model;

public class Job {
    private int jobID;
    private String jobName;
    private int duration;
    private long arrivalTime;

    public Job(int jobID, String jobName, int duration, long arrivalTime) {
        this.jobID = jobID;
        this.jobName = jobName;
        this.duration = duration;
        this.arrivalTime = arrivalTime;
    }

    public int getJobID() {
        return jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public int getDuration() {
        return duration;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String toString() {
        return jobID + "," + jobName + "," + duration + "," + arrivalTime;
    }
}
