package com.vehicularcloud.model;

public record OwnerInput(
        String ownerId,
        String vehicleMake,
        String vehicleModel,
        int vehicleYear,
        String licensePlate,
        double residencyHours
) implements ConsoleInput {
    @Override
    public Role role() {
        return Role.OWNER;
    }
}
