package com.dominik.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationUpdateRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
