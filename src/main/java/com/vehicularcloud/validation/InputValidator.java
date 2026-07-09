package com.vehicularcloud.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class InputValidator {

    private InputValidator() {
    }

    public static final DateTimeFormatter DEADLINE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }

    public static int parseInt(String raw, String fieldName, int min, int max) {
        requireNonEmpty(raw, fieldName);
        try {
            int v = Integer.parseInt(raw.trim());
            if (v < min || v > max) {
                throw new IllegalArgumentException(
                        fieldName + " must be between " + min + " and " + max + ".");
            }
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be an integer.");
        }
    }

    public static double parseDouble(String raw, String fieldName, double minExclusive) {
        requireNonEmpty(raw, fieldName);
        try {
            double v = Double.parseDouble(raw.trim());
            if (v <= minExclusive) {
                throw new IllegalArgumentException(
                        fieldName + " must be greater than " + minExclusive + ".");
            }
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    public static LocalDateTime parseDeadline(String raw, String fieldName) {
        requireNonEmpty(raw, fieldName);
        try {
            return LocalDateTime.parse(raw.trim(), DEADLINE_FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    fieldName + " must match format yyyy-MM-dd HH:mm (e.g., 2025-10-01 18:00).");
        }
    }

    public static void validateLicensePlate(String plate) {
        if (!plate.matches("[A-Za-z0-9-]{2,10}")) {
            throw new IllegalArgumentException(
                    "License Plate must be 2–10 letters/numbers (dashes allowed).");
        }
    }

    public static void validateMakeModel(String s, String fieldName) {
        requireNonEmpty(s, fieldName);
        if (s.length() > 40) {
            throw new IllegalArgumentException(fieldName + " is too long (<= 40 chars).");
        }
    }

    public static void validateId(String s, String fieldName) {
        requireNonEmpty(s, fieldName);
        if (!s.matches("[A-Za-z0-9_-]{1,20}")) {
            throw new IllegalArgumentException(
                    fieldName + " must be alphanumeric/underscore/hyphen (<=20 chars).");
        }
    }

    public static void ensureDeadlineNotPast(LocalDateTime dt) {
        if (dt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past.");
        }
    }
}
