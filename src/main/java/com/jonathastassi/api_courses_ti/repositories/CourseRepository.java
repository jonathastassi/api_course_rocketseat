package com.jonathastassi.api_courses_ti.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathastassi.api_courses_ti.entities.Course;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByNameContainingIgnoreCaseOrCategoryId(String name, UUID categoryId);
}
