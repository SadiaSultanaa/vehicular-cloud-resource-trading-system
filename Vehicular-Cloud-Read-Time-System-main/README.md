# Vehicular Cloud Console - Milestone 4

A Java-based desktop application for managing a Vehicular Cloud Resource Trading System (VCRTS). This system enables vehicle owners to register their computing resources and clients to submit computational jobs.

## Team Members

- **Christopher** - GUI Development (Part 1)
- **Ryan** - Input Validation (Part 2)
- **Sadia** - CSV Storage Management (Part 3)
- **Rezwan** - System Integration (Part 4)

## Project Overview

The Vehicular Cloud Console is a comprehensive system that allows two types of users to interact with the platform:

- **Owners**: Register their vehicles along with computing resource availability
- **Clients**: Submit computational jobs with duration and deadline requirements

All transactions are validated, timestamped, and persisted to CSV storage for future reference.

## Features

### Core Functionality

- ✅ Dual-role user interface (Owner/Client selection)
- ✅ Dynamic form switching based on selected role
- ✅ Comprehensive input validation
- ✅ Real-time error feedback with user-friendly dialogs
- ✅ CSV-based transaction persistence with automatic timestamps
- ✅ Job completion time calculation using FIFO algorithm
- ✅ VC Controller view to display job completion times
- ✅ Clean architecture with separation of concerns

### Owner Features

- Register Owner ID
- Submit vehicle details (Make, Model, Year, License Plate)
- Specify residency time (hours available for computation)

### Client Features

- Register Client ID
- Specify job duration (in hours)
- Set job deadline (with past-date validation)
- Jobs are automatically saved to jobs.csv for completion time calculation

### Storage & Reporting Features

- Transaction search by ID
- Automatic backup creation
- Formatted transaction display
- Summary reports (owner/client counts, date ranges)
- Clear all transactions (with automatic backup)
- Job completion time calculation using FIFO algorithm

### VC Controller Features

- Calculate completion times for all submitted jobs
- Display job information (ID, duration, completion time)
- Uses FIFO (First-In-First-Out) algorithm based on arrival time
- Accessible via menu: VC Controller → Calculate Completion Time

## Architecture

The application follows a clean MVC-style architecture:

```
┌─────────────────────────────────────────────────┐
│         VehicularCloudApplication (Main)        │
└────────────────┬────────────────────────────────┘
                 │
        ┌────────┴────────┐
        ▼                 ▼
   ┌────────────┐    ┌──────────────────┐
   │    GUI     │───▶│  Input Controller│
   │  (Part 1)  │    │    (Part 2)      │
   └────────────┘    └────────┬─────────┘
                              │
                     ┌────────┴─────────┐
                     ▼                  ▼
              ┌─────────────┐   ┌──────────────────┐
              │  Validator  │   │TransactionRepo   │
              └─────────────┘   └────────┬─────────┘
                                         │
                                ┌────────┴─────────┐
                                ▼                  ▼
                        ┌──────────────┐   ┌──────────────┐
                        │CSVTransaction│   │CSVStorage    │
                        │   Repo       │───▶│  Manager     │
                        └──────────────┘   └──────────────┘
```

## Project Structure

```
src/main/java/com/vehicularcloud/
├── VehicularCloudApplication.java     # Main entry point
├── gui/
│   ├── VehicularCloudGUI.java        # Swing-based user interface
│   └── VCControllerView.java         # Job completion time viewer
├── controller/
│   └── VCController.java             # Job completion time calculator
├── model/
│   ├── Role.java                     # Enum: OWNER, CLIENT
│   ├── ConsoleInput.java             # Base interface for inputs
│   ├── OwnerInput.java               # Owner data record
│   ├── ClientInput.java              # Client data record
│   ├── Job.java                      # Job data model
│   └── TransactionRepository.java    # Repository interface
├── validation/
│   ├── InputController.java          # Handles form submissions
│   └── InputValidator.java           # Validation logic
└── storage/
    ├── CSVStorageManager.java        # Low-level CSV operations
    ├── CSVTransactionRepository.java # Repository implementation
    └── JobStorageManager.java        # Job CSV storage
```

## Technologies Used

- **Java 17+** (Records, LocalDateTime, modern language features)
- **Swing** - GUI framework
- **CSV** - File-based persistent storage
- **Maven/Standard Java** - Build system

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Java Runtime Environment (JRE) 17 or higher

## Installation & Setup

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd "Milestone 2"
   ```

2. **Compile the project**

   ```bash
   javac -d target/classes src/main/java/com/vehicularcloud/**/*.java
   ```

3. **Run the application**
   ```bash
   java -cp target/classes com.vehicularcloud.VehicularCloudApplication
   ```

## Usage Guide

### Starting the Application

When you launch the application, a GUI window will appear with the title "Vehicular Cloud Console".

### Submitting as an Owner

1. Ensure the **Owner** radio button is selected (default)
2. Fill in all required fields:
   - **Owner ID**: Alphanumeric with underscores/hyphens (1-20 chars)
   - **Vehicle Make**: Up to 40 characters
   - **Vehicle Model**: Up to 40 characters
   - **Vehicle Year**: Integer between 1980-2100
   - **License Plate**: 2-10 characters (letters, numbers, dashes)
   - **Residency Time**: Positive number (hours)
3. Click **Submit** or press Enter
4. Success or error message will appear
5. Form clears automatically on success

### Submitting as a Client

1. Select the **Client** radio button
2. Fill in all required fields:
   - **Client ID**: Alphanumeric with underscores/hyphens (1-20 chars)
   - **Job Duration**: Positive number (hours)
   - **Job Deadline**: Format `YYYY-MM-DD HH:MM` (e.g., `2025-12-31 23:59`)
3. Click **Submit** or press Enter
4. Deadline must be in the future
5. Success or error message will appear
6. Form clears automatically on success

### Calculating Job Completion Times

1. From the main window, click **VC Controller** menu → **Calculate Completion Time**
2. A new window will open showing the VC Controller interface
3. Click the **Calculate Completion Time** button
4. The system will:
   - Read all jobs from jobs.csv
   - Sort them by arrival time (FIFO)
   - Calculate cumulative completion times
   - Display results in a table format

#### Completion Time Calculation Example

If you have 3 jobs with the following arrival order:

- Job ID: 345, Duration: 5 seconds → Completion Time: 00:00:05 (5 seconds)
- Job ID: 56, Duration: 2 seconds → Completion Time: 00:00:07 (7 seconds)
- Job ID: 690, Duration: 9 seconds → Completion Time: 00:00:16 (16 seconds)

**Display Format**: All durations and completion times are shown in **HH:MM:SS** format.

**Note**: Enter duration in hours in the GUI (e.g., 0.001389 for 5 seconds). The system automatically converts to seconds for calculation.

### Exiting the Application

Click the **Exit** button or close the window.

## Data Storage

### CSV File Format

Transactions are saved to `transactions.csv` with the following structure:

```
Timestamp,Role,ID,Make,Model,Year,Plate,Residency,Duration,Deadline
```

- **Owner transactions**: First 8 columns populated
- **Client transactions**: Timestamp, Role, ID, Duration, and Deadline populated

### Example Entries

```csv
2025-10-07 10:23:38,Client,client123,,,,,,12.0,2026-10-27 10:00
2025-10-07 10:24:16,Owner,owner456,Toyota,Camry,2020,ABC-1234,4.0,,
```

### Jobs File Format

Jobs are also saved to `jobs.csv` for completion time calculation:

```csv
jobID,duration,arrivalTime
345,5,1730745600000
56,2,1730745620000
690,9,1730745640000
```

**Note**: Duration is stored in seconds, and arrival time is in milliseconds since epoch.

### Backup Files

The system automatically creates `transactions_backup.csv` when clearing all transactions.

## Validation Rules

### Owner Input

- **Owner ID**: 1-20 alphanumeric characters, underscores, or hyphens
- **Vehicle Make/Model**: 1-40 characters each
- **Vehicle Year**: 1980-2100
- **License Plate**: 2-10 characters matching pattern `[A-Za-z0-9-]`
- **Residency Hours**: Must be greater than 0

### Client Input

- **Client ID**: 1-20 alphanumeric characters, underscores, or hyphens
- **Job Duration**: Must be greater than 0 hours
- **Job Deadline**:
  - Format: `YYYY-MM-DD HH:MM`
  - Must be a future date/time

## Error Handling

The application provides user-friendly error messages for:

- Empty required fields
- Invalid data formats
- Out-of-range values
- Past deadlines
- File I/O errors

## Known Issues

- None at this time

## License

Educational project for SWE course.
