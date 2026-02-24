package com.dominik.reservation.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
