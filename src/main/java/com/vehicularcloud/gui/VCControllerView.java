package com.vehicularcloud.gui;

import com.vehicularcloud.controller.VCController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * GUI for VC Controller to display job completion times.
 * Similar to ATMSimulator - provides user interface.
 * 
 * Project: VCRTS (Vehicle Cloud Real-Time System)
 * Name: Sadia
 */
public class VCControllerView extends JFrame {
    private JButton calculateButton;
    private JTextArea resultsArea;
    private VCController controller;

    /**
     * Constructs the VC controller view.
     */
    public VCControllerView() {
        setTitle("VC Controller - Calculate Completion Times");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        controller = new VCController();

        // Button panel
        JPanel buttonPanel = new JPanel();
        calculateButton = new JButton("Calculate Completion Time");
        buttonPanel.add(calculateButton);

        // Results area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        // Add components
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add listener
        calculateButton.addActionListener(new CalculateButtonListener());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Converts seconds to HH:MM:SS format
     */
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * Listener for calculate button.
     */
    private class CalculateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                // Read jobs from database
                controller.readJobsFromDatabase();

                if (controller.getJobCount() == 0) {
                    resultsArea.setText("No jobs found.\nPlease submit jobs first using Client view.");
                    return;
                }

                // Calculate completion times
                String[][] results = controller.calculateCompletionTimes();

                // Display results
                StringBuilder output = new StringBuilder();
                output.append("=== Job Completion Times (FIFO Algorithm) ===\n\n");
                output.append(String.format("%-15s %-25s %-20s %-20s\n",
                        "Job ID", "Job Name", "Duration", "Completion Time"));
                output.append("==================================================================================\n");

                for (int i = 0; i < results.length; i++) {
                    int durationSec = Integer.parseInt(results[i][2]);
                    int completionSec = Integer.parseInt(results[i][3]);

                    output.append(String.format("%-15s %-25s %-20s %-20s\n",
                            results[i][0],
                            results[i][1],
                            formatTime(durationSec),
                            formatTime(completionSec)));
                }

                resultsArea.setText(output.toString());

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(VCControllerView.this,
                        "Error reading jobs from database: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(VCControllerView.this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
