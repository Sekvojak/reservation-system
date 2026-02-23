package com.dominik.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 30, message = "username muse b 3-10 characters")
    String username,

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    String email
) {}
