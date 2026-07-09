package com.vehicularcloud.gui;

import javax.swing.*;
import java.awt.*;
import com.vehicularcloud.validation.InputController;

public class VehicularCloudGUI extends JFrame {
    // Role selection
    private JRadioButton ownerRadio, clientRadio;
    private ButtonGroup roleGroup;

    // Owner form fields
    private JTextField ownerIdField, makeField, modelField, yearField, plateField, residencyField;
    private JLabel ownerIdLabel, makeLabel, modelLabel, yearLabel, plateLabel, residencyLabel;

    // Client form fields
    private JTextField clientIdField, jobNameField, durationField, deadlineField;
    private JLabel clientIdLabel, jobNameLabel, durationLabel, deadlineLabel;

    // Form panel
    private JPanel formPanel;

    // Buttons
    private JButton submitButton, exitButton;

    // Input controller for validation (Part 2 integration)
    private InputController inputController;

    public VehicularCloudGUI(InputController inputController) {
        this.inputController = inputController;
        // Frame setup
        setTitle("Vehicular Cloud Console");
        setSize(600, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        createMenuBar();

        // Create components
        createHeaderPanel();
        createRolePanel();
        createFormPanel();
        createButtonPanel();

        // Show owner form by default
        showOwnerForm();

        // Make submit the default button (Enter)
        getRootPane().setDefaultButton(submitButton);

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu vcMenu = new JMenu("VC Controller");

        JMenuItem calculateItem = new JMenuItem("Calculate Completion Time");
        calculateItem.addActionListener(e -> openVCControllerView());

        vcMenu.add(calculateItem);
        menuBar.add(vcMenu);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void openVCControllerView() {
        new VCControllerView();
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Vehicular Cloud Console\n" +
                        "Version 1.0\n\n" +
                        "Client can submit jobs to VC\n" +
                        "Vehicle Owner can register in the system\n" +
                        "VC Controller calculates job completion times using FIFO",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));

        JLabel titleLabel = new JLabel("Vehicular Cloud Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createRolePanel() {
        JPanel rolePanel = new JPanel();
        rolePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel roleLabel = new JLabel("Select Role: ");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        ownerRadio = new JRadioButton("Owner", true);
        clientRadio = new JRadioButton("Client");

        roleGroup = new ButtonGroup();
        roleGroup.add(ownerRadio);
        roleGroup.add(clientRadio);

        // Add action listeners
        ownerRadio.addActionListener(e -> showOwnerForm());
        clientRadio.addActionListener(e -> showClientForm());

        rolePanel.add(roleLabel);
        rolePanel.add(ownerRadio);
        rolePanel.add(clientRadio);

        // Move role panel to top (below header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(rolePanel, BorderLayout.NORTH);

        // Add form panel container
        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        topPanel.add(formPanel, BorderLayout.CENTER);

        // Add the combined topPanel to the frame (only this)
        add(topPanel, BorderLayout.CENTER);
    }

    private void createFormPanel() {
        // Initialize all form components

        // Owner fields
        ownerIdLabel = new JLabel("Owner ID:");
        ownerIdField = new JTextField(20);

        makeLabel = new JLabel("Vehicle Make:");
        makeField = new JTextField(20);

        modelLabel = new JLabel("Vehicle Model:");
        modelField = new JTextField(20);

        yearLabel = new JLabel("Vehicle Year:");
        yearField = new JTextField(20);

        plateLabel = new JLabel("License Plate:");
        plateField = new JTextField(20);

        residencyLabel = new JLabel("Residency Time (hours):");
        residencyField = new JTextField(20);

        // Client fields
        clientIdLabel = new JLabel("Client ID:");
        clientIdField = new JTextField(20);

        jobNameLabel = new JLabel("Job Name:");
        jobNameField = new JTextField(20);

        durationLabel = new JLabel("Job Duration (hours):");
        durationField = new JTextField(20);

        deadlineLabel = new JLabel("Job Deadline (YYYY-MM-DD HH:MM):");
        deadlineField = new JTextField(20);
    }

    private void showOwnerForm() {
        formPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Owner ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(ownerIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(ownerIdField, gbc);

        // Vehicle Make
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(makeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(makeField, gbc);

        // Vehicle Model
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(modelLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(modelField, gbc);

        // Vehicle Year
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);

        // License Plate
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(plateLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(plateField, gbc);

        // Residency Time
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(residencyLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(residencyField, gbc);

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void showClientForm() {
        formPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Client ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(clientIdLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(clientIdField, gbc);

        // Job Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(jobNameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(jobNameField, gbc);

        // Job Duration
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(durationLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(durationField, gbc);

        // Job Deadline
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(deadlineLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(deadlineField, gbc);

        formPanel.revalidate();
        formPanel.repaint();
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(76, 175, 80));
        submitButton.setForeground(Color.WHITE);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.addActionListener(e -> submitData());

        exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setOpaque(true);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(120, 35));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void submitData() {
        if (ownerRadio.isSelected()) {
            // Basic validation - only check for empty fields
            if (ownerIdField.getText().trim().isEmpty() ||
                    makeField.getText().trim().isEmpty() ||
                    modelField.getText().trim().isEmpty() ||
                    yearField.getText().trim().isEmpty() ||
                    plateField.getText().trim().isEmpty() ||
                    residencyField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please fill all fields!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Part 2 integration: Handle owner submission with validation
            inputController.handleOwnerSubmit(ownerIdField, makeField, modelField,
                    yearField, plateField, residencyField);
            return; // Exit early since InputController handles success/error messages

        } else {
            // Basic validation - only check for empty fields
            if (clientIdField.getText().trim().isEmpty() ||
                    jobNameField.getText().trim().isEmpty() ||
                    durationField.getText().trim().isEmpty() ||
                    deadlineField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please fill all fields!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Part 2 integration: Handle client submission with validation
            inputController.handleClientSubmit(clientIdField, jobNameField, durationField, deadlineField);
            return; // Exit early since InputController handles success/error messages
        }
    }
}
