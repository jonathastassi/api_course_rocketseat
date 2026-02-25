package com.jonathastassi.api_courses_ti.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(
                String name,
                @Email String email,
                String password,
                @NotBlank String role) {
}
