-- VCRTS Database Schema
-- Member 3: Database Design

CREATE DATABASE IF NOT EXISTS vcrts_db;
USE vcrts_db;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS vehicle_owners;

-- Jobs table: stores task owner job submissions
CREATE TABLE jobs (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    client_id     VARCHAR(50) NOT NULL,
    job_name      VARCHAR(100) NOT NULL,
    duration      DOUBLE NOT NULL,
    deadline      DATETIME NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicle Owners table: stores vehicle owner registrations
CREATE TABLE vehicle_owners (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    owner_id        VARCHAR(50) NOT NULL,
    vehicle_make    VARCHAR(100) NOT NULL,
    vehicle_model   VARCHAR(100) NOT NULL,
    vehicle_year    INT NOT NULL,
    license_plate   VARCHAR(20) NOT NULL,
    residency_hours DOUBLE NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for faster lookups
CREATE INDEX idx_jobs_client_id ON jobs(client_id);
CREATE INDEX idx_vehicle_owners_owner_id ON vehicle_owners(owner_id);
