package com.jonathastassi.api_courses_ti.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jonathastassi.api_courses_ti.dtos.CreateCourseRequestDto;
import com.jonathastassi.api_courses_ti.dtos.UpdateCourseRequestDto;
import com.jonathastassi.api_courses_ti.entities.Category;
import com.jonathastassi.api_courses_ti.entities.Course;
import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;
import com.jonathastassi.api_courses_ti.repositories.CategoryRepository;
import com.jonathastassi.api_courses_ti.repositories.CourseRepository;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private UUID categoryId;
    private UUID teacherId;
    private UUID courseId;
    private Category category;
    private User teacher;
    private Course course;
    private CreateCourseRequestDto createRequest;
    private UpdateCourseRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        teacherId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        category = Category.builder()
                .id(categoryId)
                .name("Programming")
                .build();

        teacher = User.builder()
                .id(teacherId)
                .name("John Doe")
                .role(UserRoleEnum.TEACHER)
                .build();

        course = Course.builder()
                .id(courseId)
                .name("Java Course")
                .active(true)
                .category(category)
                .teacher(teacher)
                .build();

        createRequest = new CreateCourseRequestDto(
                "Java Course",
                categoryId.toString(),
                teacherId.toString()
        );

        updateRequest = new UpdateCourseRequestDto(
                "Updated Java Course",
                categoryId.toString(),
                teacherId.toString()
        );
    }

    @Test
    @DisplayName("Should create course successfully")
    void createCourse_Success() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(userRepository.findByIdAndRole(teacherId, UserRoleEnum.TEACHER)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        Course result = courseService.createCourse(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Java Course", result.getName());
        assertTrue(result.isActive());
        assertEquals(category, result.getCategory());
        assertEquals(teacher, result.getTeacher());

        verify(categoryRepository).findById(categoryId);
        verify(userRepository).findByIdAndRole(teacherId, UserRoleEnum.TEACHER);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("Should throw exception when category not found during course creation")
    void createCourse_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.createCourse(createRequest));
        
        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(categoryId);
        verify(userRepository, never()).findByIdAndRole(any(), any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when teacher not found during course creation")
    void createCourse_TeacherNotFound() {
        // Arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(userRepository.findByIdAndRole(teacherId, UserRoleEnum.TEACHER)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.createCourse(createRequest));
        
        assertEquals("Teacher not found", exception.getMessage());
        verify(categoryRepository).findById(categoryId);
        verify(userRepository).findByIdAndRole(teacherId, UserRoleEnum.TEACHER);
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update course successfully with all fields")
    void updateCourse_AllFields_Success() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(userRepository.findByIdAndRole(teacherId, UserRoleEnum.TEACHER)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course result = courseService.updateCourse(courseId, updateRequest);

        // Assert
        assertNotNull(result);
        verify(courseRepository).findById(courseId);
        verify(categoryRepository).findById(categoryId);
        verify(userRepository).findByIdAndRole(teacherId, UserRoleEnum.TEACHER);
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should update course with only name")
    void updateCourse_OnlyName_Success() {
        // Arrange
        UpdateCourseRequestDto nameOnlyRequest = new UpdateCourseRequestDto(
                "New Course Name", null, null);
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course result = courseService.updateCourse(courseId, nameOnlyRequest);

        // Assert
        assertNotNull(result);
        verify(courseRepository).findById(courseId);
        verify(categoryRepository, never()).findById(any());
        verify(userRepository, never()).findByIdAndRole(any(), any());
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should throw exception when course not found during update")
    void updateCourse_CourseNotFound() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.updateCourse(courseId, updateRequest));
        
        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
        verify(categoryRepository, never()).findById(any());
        verify(userRepository, never()).findByIdAndRole(any(), any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when category not found during update")
    void updateCourse_CategoryNotFound() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.updateCourse(courseId, updateRequest));
        
        assertEquals("Category not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
        verify(categoryRepository).findById(categoryId);
        verify(userRepository, never()).findByIdAndRole(any(), any());
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when teacher not found during update")
    void updateCourse_TeacherNotFound() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(userRepository.findByIdAndRole(teacherId, UserRoleEnum.TEACHER)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.updateCourse(courseId, updateRequest));
        
        assertEquals("Teacher not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
        verify(categoryRepository).findById(categoryId);
        verify(userRepository).findByIdAndRole(teacherId, UserRoleEnum.TEACHER);
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get all courses when no filters provided")
    void getAllCourses_NoFilters_Success() {
        // Arrange
        List<Course> courses = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(courses);

        // Act
        List<Course> result = courseService.getAllCourses(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
        verify(courseRepository).findAll();
        verify(courseRepository, never()).findByNameContainingIgnoreCaseOrCategoryId(any(), any());
    }

    @Test
    @DisplayName("Should get courses with filters")
    void getAllCourses_WithFilters_Success() {
        // Arrange
        String courseName = "Java";
        String categoryIdStr = categoryId.toString();
        List<Course> courses = Arrays.asList(course);
        
        when(courseRepository.findByNameContainingIgnoreCaseOrCategoryId(courseName, categoryId))
                .thenReturn(courses);

        // Act
        List<Course> result = courseService.getAllCourses(courseName, categoryIdStr);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
        verify(courseRepository).findByNameContainingIgnoreCaseOrCategoryId(courseName, categoryId);
        verify(courseRepository, never()).findAll();
    }

    @Test
    @DisplayName("Should get courses with only course name filter")
    void getAllCourses_OnlyCourseName_Success() {
        // Arrange
        String courseName = "Java";
        List<Course> courses = Arrays.asList(course);
        
        when(courseRepository.findByNameContainingIgnoreCaseOrCategoryId(courseName, null))
                .thenReturn(courses);

        // Act
        List<Course> result = courseService.getAllCourses(courseName, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(courseRepository).findByNameContainingIgnoreCaseOrCategoryId(courseName, null);
    }

    @Test
    @DisplayName("Should delete course successfully")
    void deleteCourse_Success() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        verify(courseRepository).findById(courseId);
        verify(courseRepository).delete(course);
    }

    @Test
    @DisplayName("Should throw exception when course not found during deletion")
    void deleteCourse_CourseNotFound() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.deleteCourse(courseId));
        
        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should toggle course active status from true to false")
    void toggleActive_FromTrueToFalse_Success() {
        // Arrange
        course.setActive(true);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course result = courseService.tooggleActive(courseId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should toggle course active status from false to true")
    void toggleActive_FromFalseToTrue_Success() {
        // Arrange
        course.setActive(false);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        // Act
        Course result = courseService.tooggleActive(courseId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isActive());
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should throw exception when course not found during toggle active")
    void toggleActive_CourseNotFound() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> courseService.tooggleActive(courseId));
        
        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any());
    }
}
