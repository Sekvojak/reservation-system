package com.dominik.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @NotNull @Positive Long facilityId,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
