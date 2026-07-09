# SQL Commands Reference

## Connect to MySQL

```bash
mysql -u root -p
```

---

## Database Setup

### Create Database and Tables

```sql
CREATE DATABASE IF NOT EXISTS vcrts_db;
USE vcrts_db;

-- Jobs table: stores task owner job submissions
CREATE TABLE IF NOT EXISTS jobs (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    client_id     VARCHAR(50) NOT NULL,
    job_name      VARCHAR(100) NOT NULL,
    duration      DOUBLE NOT NULL,
    deadline      DATETIME NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicle Owners table: stores vehicle owner registrations
CREATE TABLE IF NOT EXISTS vehicle_owners (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    owner_id        VARCHAR(50) NOT NULL,
    vehicle_make    VARCHAR(100) NOT NULL,
    vehicle_model   VARCHAR(100) NOT NULL,
    vehicle_year    INT NOT NULL,
    license_plate   VARCHAR(20) NOT NULL,
    residency_hours DOUBLE NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## View Data

### Select Database

```sql
USE vcrts_db;
```

### View All Jobs

```sql
SELECT * FROM jobs;
```

### View All Vehicle Owners

```sql
SELECT * FROM vehicle_owners;
```

### View Jobs (Formatted)

```sql
SELECT id, client_id, job_name, duration,
       DATE_FORMAT(deadline, '%Y-%m-%d %H:%i') AS deadline,
       created_at
FROM jobs
ORDER BY created_at DESC;
```

### View Vehicle Owners (Formatted)

```sql
SELECT id, owner_id,
       CONCAT(vehicle_year, ' ', vehicle_make, ' ', vehicle_model) AS vehicle,
       license_plate, residency_hours, created_at
FROM vehicle_owners
ORDER BY created_at DESC;
```

---

## Table Info

### Show Table Structure

```sql
DESCRIBE jobs;
DESCRIBE vehicle_owners;
```

### Count Records

```sql
SELECT COUNT(*) AS total_jobs FROM jobs;
SELECT COUNT(*) AS total_vehicle_owners FROM vehicle_owners;
```

---

## Clear Data

### Delete All Records (Keep Tables)

```sql
DELETE FROM jobs;
DELETE FROM vehicle_owners;
```

### Reset Tables (Clears Data + Resets Auto-Increment IDs)

```sql
TRUNCATE TABLE jobs;
TRUNCATE TABLE vehicle_owners;
```

### Drop Tables Completely

```sql
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS vehicle_owners;
```

### Drop Entire Database

```sql
DROP DATABASE IF EXISTS vcrts_db;
```
