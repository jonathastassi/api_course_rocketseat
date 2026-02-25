package com.jonathastassi.api_courses_ti.dtos;

public record CreateCourseResponseDto(
        String id,
        String name,
        String category_id,
        String teacher_id) {

}
