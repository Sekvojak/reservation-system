package com.dominik.reservation.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long userId,
        Long facilityId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
