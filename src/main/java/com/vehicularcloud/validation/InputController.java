package com.vehicularcloud.validation;

import javax.swing.*;
import com.vehicularcloud.model.*;
import com.vehicularcloud.storage.JobStorageManager;
import com.vehicularcloud.network.VehicleOwnerClient;
import com.vehicularcloud.network.JobClient;

public class InputController {

    private final TransactionRepository repo;
    private final JobStorageManager jobStorage;

    public InputController(TransactionRepository repo) {
        this.repo = repo;
        this.jobStorage = new JobStorageManager();
    }

    // OWNER
    public void handleOwnerSubmit(
            JTextField ownerIdField,
            JTextField makeField,
            JTextField modelField,
            JTextField yearField,
            JTextField licenseField,
            JTextField residencyHoursField) {

        try {
            // read input from fields
            String ownerId = ownerIdField.getText();
            String make = makeField.getText();
            String model = modelField.getText();
            String plate = licenseField.getText();
            String yearRaw = yearField.getText();
            String residRaw = residencyHoursField.getText();

            // validate input
            InputValidator.validateId(ownerId, "Owner ID");
            InputValidator.validateMakeModel(make, "Vehicle Make");
            InputValidator.validateMakeModel(model, "Vehicle Model");
            InputValidator.validateLicensePlate(plate);
            int year = InputValidator.parseInt(yearRaw, "Vehicle Year", 1980, 2100);
            double residencyHours = InputValidator.parseDouble(residRaw, "Residency Time (hours)", 0.0);

            // create owner object
            OwnerInput owner = new OwnerInput(ownerId, make, model, year, plate, residencyHours);

            // run network call in separate thread
            new Thread(() -> {
                VehicleOwnerClient client = new VehicleOwnerClient();
                String serverResponse = client.sendOwnerToServer(owner);

                // update GUI on Swing thread
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if ("ACCEPTED".equalsIgnoreCase(serverResponse)) {
                        showInfo("VC Controller accepted the vehicle owner registration.");
                        clearFields(ownerIdField, makeField, modelField, yearField, licenseField, residencyHoursField);
                    } else if ("REJECTED".equalsIgnoreCase(serverResponse)) {
                        showError("VC Controller rejected the vehicle owner registration.");
                    } else {
                        showError(serverResponse);
                    }
                });
            }).start();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    // CLIENT
    public void handleClientSubmit(
            JTextField clientIdField,
            JTextField jobNameField,
            JTextField durationHoursField,
            JTextField deadlineField) {
        try {
            // read input from fields
            String clientId = clientIdField.getText();
            String jobName = jobNameField.getText();
            String durRaw = durationHoursField.getText();
            String deadlineRaw = deadlineField.getText();

            // validate input
            InputValidator.validateId(clientId, "Client ID");
            InputValidator.validateMakeModel(jobName, "Job Name");
            double duration = InputValidator.parseDouble(durRaw, "Job Duration (hours)", 0.0);
            var deadline = InputValidator.parseDeadline(deadlineRaw, "Job Deadline");
            InputValidator.ensureDeadlineNotPast(deadline);

            // Format deadline for network transmission
            String deadlineFormatted = deadline.format(InputValidator.DEADLINE_FMT);

            // run network call in separate thread
            new Thread(() -> {
                JobClient client = new JobClient();
                String serverResponse = client.sendJobToServer(
                    clientId,
                    jobName,
                    String.valueOf(duration),
                    deadlineFormatted
                );

                // update GUI on Swing thread
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if ("ACCEPTED".equalsIgnoreCase(serverResponse)) {
                        showInfo("VC Controller accepted the task owner job submission.");
                        clearFields(clientIdField, jobNameField, durationHoursField, deadlineField);
                    } else if ("REJECTED".equalsIgnoreCase(serverResponse)) {
                        showError("VC Controller rejected the task owner job submission.");
                    } else {
                        showError(serverResponse);
                    }
                });
            }).start();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    // Helpers
    private void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields)
            f.setText("");
    }
}
