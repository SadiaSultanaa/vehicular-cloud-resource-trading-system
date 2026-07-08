package com.vehicularcloud.model;

import java.time.LocalDateTime;

public record ClientInput(
        String clientId,
        String jobName,
        double jobDurationHours,
        LocalDateTime jobDeadline
) implements ConsoleInput {
    @Override
    public Role role() {
        return Role.CLIENT;
    }
}
