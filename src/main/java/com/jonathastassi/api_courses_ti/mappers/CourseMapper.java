package com.jonathastassi.api_courses_ti.mappers;

import org.springframework.stereotype.Component;

import com.jonathastassi.api_courses_ti.dtos.CreateCourseResponseDto;
import com.jonathastassi.api_courses_ti.dtos.ListCourseResponseDto;
import com.jonathastassi.api_courses_ti.entities.Course;

@Component
public class CourseMapper {
    public CreateCourseResponseDto toResponseDto(Course course) {
        return new CreateCourseResponseDto(course.getId().toString(), course.getName(),
                course.getCategory().getId().toString(), course.getTeacher().getId().toString());
    }

    public ListCourseResponseDto toListResponseDto(Course course) {
        return new ListCourseResponseDto(course.getId().toString(),
                course.getName(),
                course.isActive(),
                course.getCategory().getId().toString(),
                course.getTeacher().getId().toString(),
                course.getCategory().getName(),
                course.getTeacher().getName());
    }
}
