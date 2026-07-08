package com.vehicularcloud;

import com.vehicularcloud.gui.WelcomePage;
import com.vehicularcloud.validation.InputController;
import com.vehicularcloud.storage.CSVStorageManager;
import com.vehicularcloud.storage.CSVTransactionRepository;

import javax.swing.*;

/**
 * Main application class for the Vehicular Cloud Console
 * 
 * This class initializes and coordinates all parts of the system:
 * - Part 1: GUI (VehicularCloudGUI)
 * - Part 2: Validation (InputController + InputValidator)
 * - Part 3: Storage (CSVStorageManager)
 * 
 * @author Team: Christopher (Q1), Ryan (Q2), Sadia (Q3), Rezwan (Q4)
 */
public class VehicularCloudApplication {

    public static void main(String[] args) {
        // Initialize the application
        SwingUtilities.invokeLater(() -> {
            try {
                // Create CSV storage manager (Part 3)
                CSVStorageManager csvManager = new CSVStorageManager();

                // Create CSV repository (Part 2 + Part 3 integration)
                CSVTransactionRepository repository = new CSVTransactionRepository(csvManager);

                // Create input controller (Part 2)
                InputController inputController = new InputController(repository);

                // Show welcome page first, then main GUI
                new WelcomePage(inputController);

                System.out.println("Vehicular Cloud Console started successfully!");
                System.out.println("Welcome!");

            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
