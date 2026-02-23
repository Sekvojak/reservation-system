package com.dominik.reservation.dto;

import com.dominik.reservation.entity.FacilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FacilityCreateRequest(
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 60, message = "name must be 3-60 characters")
    String name,

    @NotNull(message = "type is required")
    FacilityType type
) {}


