package com.jonathastassi.api_courses_ti.dtos;

public record ListCourseResponseDto(
                String id,
                String name,
                boolean active,
                String categoryId,
                String teacherId,
                String categoryName,
                String teacherName) {
}
