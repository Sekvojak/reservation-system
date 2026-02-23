package com.dominik.reservation.dto;

import java.time.LocalDateTime;

public record ReservationCreateRequest(
        Long userId,
        Long facilityId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
