package com.dominik.reservation.dto;

import com.dominik.reservation.entity.FacilityType;

import java.time.LocalDateTime;

public record FacilityResponse(
    Long id,
    String name,
    FacilityType type,
    LocalDateTime createdAt
) {
}
