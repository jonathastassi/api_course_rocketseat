package com.jonathastassi.api_courses_ti.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonathastassi.api_courses_ti.dtos.CreateCourseRequestDto;
import com.jonathastassi.api_courses_ti.dtos.UpdateCourseRequestDto;
import com.jonathastassi.api_courses_ti.entities.Category;
import com.jonathastassi.api_courses_ti.entities.Course;
import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;
import com.jonathastassi.api_courses_ti.repositories.CategoryRepository;
import com.jonathastassi.api_courses_ti.repositories.CourseRepository;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@Service
public class CourseService {
        @Autowired
        private CourseRepository courseRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private UserRepository userRepository;

        public Course createCourse(CreateCourseRequestDto request) {

                Category category = categoryRepository.findById(UUID.fromString(request.category_id()))
                                .orElseThrow(() -> new RuntimeException("Category not found"));

                User teacher = userRepository
                                .findByIdAndRole(UUID.fromString(request.teacher_id()), UserRoleEnum.TEACHER)
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                Course course = Course.builder()
                                .name(request.name())
                                .active(true)
                                .category(category)
                                .teacher(teacher)
                                .build();

                return courseRepository.save(course);
        }

        public Course updateCourse(UUID id, UpdateCourseRequestDto request) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Course not found"));

                if (request.name() != null) {
                        course.setName(request.name());
                }

                if (request.category_id() != null) {
                        Category category = categoryRepository.findById(UUID.fromString(request.category_id()))
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        course.setCategory(category);
                }

                if (request.teacher_id() != null) {
                        User teacher = userRepository
                                        .findByIdAndRole(UUID.fromString(request.teacher_id()), UserRoleEnum.TEACHER)
                                        .orElseThrow(() -> new RuntimeException("Teacher not found"));
                        course.setTeacher(teacher);
                }

                return courseRepository.save(course);
        }

        public List<Course> getAllCourses(
                        String courseName,
                        String categoryId) {
                if (courseName == null && categoryId == null) {
                        return courseRepository.findAll();
                }

                return courseRepository.findByNameContainingIgnoreCaseOrCategoryId(
                                courseName, categoryId == null ? null : UUID.fromString(categoryId));
        }

        public void deleteCourse(UUID id) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Course not found"));
                courseRepository.delete(course);
        }

        public Course tooggleActive(UUID id) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Course not found"));
                course.setActive(!course.isActive());
                return courseRepository.save(course);
        }
}
