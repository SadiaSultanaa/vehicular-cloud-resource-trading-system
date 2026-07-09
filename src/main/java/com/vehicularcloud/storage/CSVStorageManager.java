package com.vehicularcloud.storage;

import java.io.*;

/**
 * Question (3): CSV Storage Manager for VCRTS (FINAL UPDATED)
 * ------------------------------------------------------------
 * This part of the project handles how data gets saved to and read from files.
 * My job (Sadia - Q3) was to take the inputs validated by Ryan’s part (Q2)
 * and save them properly with timestamps that Chris’s GUI (Q1) provides.
 * Rezwan (Q4) will integrate and test all parts together.
 *
 * Name: Sadia Sultana
 * Team: Christopher (Q1), Ryan (Q2), Sadia (Q3), Rezwan (Q4)
 */
public class CSVStorageManager {

    // I created these constants for the main CSV file and its backup copy
    private static final String FILE_NAME = "transactions.csv";
    private static final String BACKUP_FILE = "transactions_backup.csv";

    // I use this variable to store the latest error message for debugging
    private String lastErrorMessage = "";

    /**
     * Constructor – when this class is called, it checks if the CSV file exists.
     * If not, I make sure it creates one with the right headers.
     */
    public CSVStorageManager() {
        initializeFile();
    }

    /**
     * This method sets up the file the first time.
     * It adds the column headers that match Rizwan’s required data structure.
     */
    private void initializeFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME))) {
                out.write("Timestamp,Role,ID,Make,Model,Year,Plate,Residency,Duration,Deadline");
                out.newLine();
                System.out.println("CSV file created with headers: " + FILE_NAME);
            } catch (IOException e) {
                setError("Error creating CSV file: " + e.getMessage());
            }
        }
    }

    // ============================================================
    // SAVE METHODS
    // ============================================================

    /**
     * This method saves an Owner's transaction data into the CSV file.
     * It includes the timestamp (from Chris's part), owner details, and residency
     * info.
     * Thread-safe for concurrent server requests.
     */
    public synchronized boolean saveOwnerTransaction(String timestamp, String ownerId, String make, String model,
            String year, String plate, String residency) {
        try {
            // I format the owner data exactly how the CSV header expects it
            String record = String.format("%s,Owner,%s,%s,%s,%s,%s,%s,,",
                    escapeCsvValue(timestamp),
                    escapeCsvValue(ownerId),
                    escapeCsvValue(make),
                    escapeCsvValue(model),
                    escapeCsvValue(year),
                    escapeCsvValue(plate),
                    escapeCsvValue(residency));

            appendToFile(record);
            System.out.println("Owner transaction saved successfully!");
            return true;

        } catch (Exception e) {
            setError("Error saving owner transaction: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method saves a Client's transaction data into the CSV file.
     * It stores client ID, job name, job duration, and deadline.
     * Thread-safe for concurrent server requests.
     */
    public synchronized boolean saveClientTransaction(String timestamp, String clientId, String jobName, String duration, String deadline) {
        try {
            // I follow the same column order so everything stays aligned in the CSV
            // Job name is stored in the Make column (column 3) for clients
            String record = String.format("%s,Client,%s,%s,,,,,%s,%s",
                    escapeCsvValue(timestamp),
                    escapeCsvValue(clientId),
                    escapeCsvValue(jobName),
                    escapeCsvValue(duration),
                    escapeCsvValue(deadline));

            appendToFile(record);
            System.out.println("Client transaction saved successfully!");
            return true;

        } catch (Exception e) {
            setError("Error saving client transaction: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // FILE HANDLING
    // ============================================================

    /**
     * This helper method appends new lines of data to the CSV file.
     * I used BufferedWriter here so data gets written efficiently.
     */
    private void appendToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    /**
     * This method helps prevent CSV errors by escaping commas or quotes in input.
     */
    private String escapeCsvValue(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // ============================================================
    // EXTRA FEATURES
    // ============================================================

    /**
     * I added this method to search transactions by Owner or Client ID.
     * It scans through the file and prints matching results.
     */
    public void searchById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            reader.readLine(); // skip header
            String line;
            boolean found = false;
            System.out.println("\n===== Search Results for ID: " + id + " =====");
            while ((line = reader.readLine()) != null) {
                if (line.contains(id)) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found)
                System.out.println("No transactions found for ID: " + id);
            System.out.println("==========================================\n");
        } catch (IOException e) {
            setError("Error searching by ID: " + e.getMessage());
        }
    }

    /**
     * This creates a backup copy of the current CSV file.
     * I made it to avoid data loss when we clear or reset the file.
     */
    public void exportToBackup() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
                BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data exported to backup file: " + BACKUP_FILE);
        } catch (IOException e) {
            setError("Error exporting to backup: " + e.getMessage());
        }
    }

    /**
     * I used this method to print all stored transactions in a neat table format.
     * It’s mostly for testing and readability.
     */
    public void displayTransactionsFormatted() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String header = reader.readLine();
            if (header == null) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("\n===== Formatted Transactions =====");
            System.out.printf("%-20s %-8s %-10s %-20s %-12s %-20s%n",
                    "Timestamp", "Role", "ID", "Vehicle/Job", "Duration", "Deadline/Residency");
            System.out.println("--------------------------------------------------------------------------------");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                String timestamp = parts[0];
                String role = parts[1];
                String id = parts[2];

                if (role.equalsIgnoreCase("Owner")) {
                    String vehicle = parts[3] + " " + parts[4];
                    String residency = parts[7];
                    System.out.printf("%-20s %-8s %-10s %-20s %-12s %-20s%n",
                            timestamp, role, id, vehicle, "", residency + " hrs");
                } else if (role.equalsIgnoreCase("Client")) {
                    String duration = parts[8];
                    String deadline = parts[9];
                    System.out.printf("%-20s %-8s %-10s %-20s %-12s %-20s%n",
                            timestamp, role, id, "", duration + " hrs", deadline);
                }
            }
            System.out.println("==================================\n");
        } catch (IOException e) {
            setError("Error reading transactions: " + e.getMessage());
        }
    }

    /**
     * This gives a short summary of all transactions.
     * It counts owners, clients, and shows the first and most recent timestamps.
     */
    public void summaryReport() {
        int owners = 0, clients = 0, total = 0;
        String first = "", last = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                total++;
                String[] parts = line.split(",", -1);
                String timestamp = parts[0];
                String role = parts[1];

                if (role.equalsIgnoreCase("Owner"))
                    owners++;
                if (role.equalsIgnoreCase("Client"))
                    clients++;

                if (first.isEmpty())
                    first = timestamp;
                last = timestamp;
            }

            System.out.println("\n===== Summary Report =====");
            System.out.println("Total transactions: " + total);
            System.out.println("Owners: " + owners);
            System.out.println("Clients: " + clients);
            if (!first.isEmpty()) {
                System.out.println("First transaction: " + first);
                System.out.println("Most recent transaction: " + last);
            }
            System.out.println("==========================\n");

        } catch (IOException e) {
            setError("Error creating summary report: " + e.getMessage());
        }
    }

    /**
     * I added this so we can clear the file safely without losing data.
     * It backs up the file before deleting the original one.
     */
    public void clearAllTransactions() {
        exportToBackup();
        File file = new File(FILE_NAME);
        if (file.exists() && file.delete()) {
            initializeFile();
            System.out.println("All transactions cleared (backup created)!");
        } else {
            setError("Error clearing transactions.");
        }
    }

    // ============================================================
    // UTILITIES
    // ============================================================

    // This records an error message and prints it to the console
    private void setError(String message) {
        this.lastErrorMessage = message;
        System.err.println(message);
    }

    // This just counts the total number of saved transactions
    public int getTransactionCount() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            reader.readLine();
            while (reader.readLine() != null)
                count++;
        } catch (IOException e) {
            setError("Error counting transactions: " + e.getMessage());
        }
        return count;
    }

    // This returns the most recent error message
    public String getLastError() {
        return lastErrorMessage;
    }

    // This checks if the main file exists
    public boolean fileExists() {
        return new File(FILE_NAME).exists();
    }

    // This returns the main file name
    public String getFileName() {
        return FILE_NAME;
    }
}
