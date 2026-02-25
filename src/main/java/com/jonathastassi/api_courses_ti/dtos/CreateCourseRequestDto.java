package com.jonathastassi.api_courses_ti.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateCourseRequestDto(
        @NotBlank String name,
        @NotBlank String category_id,
        @NotBlank String teacher_id) {
}