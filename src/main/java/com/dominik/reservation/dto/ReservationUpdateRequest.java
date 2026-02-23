package com.dominik.reservation.dto;

import java.time.LocalDateTime;

public record ReservationUpdateRequest(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
