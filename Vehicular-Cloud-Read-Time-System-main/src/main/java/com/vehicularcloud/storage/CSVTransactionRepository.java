package com.vehicularcloud.storage;

import com.vehicularcloud.model.*;
import com.vehicularcloud.validation.InputValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CSV implementation of TransactionRepository
 * Bridges Part 2 (Validation) and Part 3 (Storage)
 */
public class CSVTransactionRepository implements TransactionRepository {

    private final CSVStorageManager csvManager;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CSVTransactionRepository(CSVStorageManager csvManager) {
        this.csvManager = csvManager;
    }

    @Override
    public synchronized void saveOwner(OwnerInput owner) {
        String timestamp = getCurrentTimestamp();
        csvManager.saveOwnerTransaction(
                timestamp,
                owner.ownerId(),
                owner.vehicleMake(),
                owner.vehicleModel(),
                String.valueOf(owner.vehicleYear()),
                owner.licensePlate(),
                String.valueOf(owner.residencyHours()));
    }

    @Override
    public synchronized void saveClient(ClientInput client) {
        String timestamp = getCurrentTimestamp();

        // Null-safe + consistent with validator format
        String deadlineStr = (client.jobDeadline() == null)
                ? ""
                : client.jobDeadline().format(InputValidator.DEADLINE_FMT);

        csvManager.saveClientTransaction(
                timestamp,
                client.clientId(),
                client.jobName(),
                String.valueOf(client.jobDurationHours()),
                deadlineStr);
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }
}
