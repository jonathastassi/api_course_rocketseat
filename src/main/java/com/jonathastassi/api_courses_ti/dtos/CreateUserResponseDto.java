package com.jonathastassi.api_courses_ti.dtos;

import java.util.UUID;

public record CreateUserResponseDto(
        UUID id,
        String name,
        String email,
        String role) {

}
