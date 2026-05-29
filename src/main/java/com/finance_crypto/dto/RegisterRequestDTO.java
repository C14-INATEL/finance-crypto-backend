package com.finance_crypto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank String username,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password) {}
