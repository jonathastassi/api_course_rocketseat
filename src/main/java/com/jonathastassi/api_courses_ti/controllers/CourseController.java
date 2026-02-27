package com.jonathastassi.api_courses_ti.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathastassi.api_courses_ti.dtos.CreateCourseRequestDto;
import com.jonathastassi.api_courses_ti.dtos.CreateCourseResponseDto;
import com.jonathastassi.api_courses_ti.dtos.ListCourseResponseDto;
import com.jonathastassi.api_courses_ti.dtos.UpdateCourseRequestDto;
import com.jonathastassi.api_courses_ti.entities.Course;
import com.jonathastassi.api_courses_ti.mappers.CourseMapper;
import com.jonathastassi.api_courses_ti.services.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/courses")
@Tags({
                @Tag(name = "Courses", description = "Endpoints relacionados a cursos"),
})
@SecurityRequirements({
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
})
public class CourseController {

        @Autowired
        private CourseService courseService;

        @Autowired
        private CourseMapper courseMapper;

        @PostMapping
        @Operation(summary = "Criar curso", description = "Endpoint para criar um novo curso. Requer nome, id da categoria e id do professor.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Curso criado com sucesso"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Requisição inválida, como categoria ou professor não encontrados")
        })

        public ResponseEntity<CreateCourseResponseDto> createCourse(
                        @Valid @RequestBody CreateCourseRequestDto entity) {
                CreateCourseResponseDto createdCourse = courseMapper.toResponseDto(courseService.createCourse(entity));
                return ResponseEntity.ok(createdCourse);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ListCourseResponseDto> updateCourse(
                        @PathVariable String id, @RequestBody UpdateCourseRequestDto entity) {
                Course updatedCourse = courseService.updateCourse(UUID.fromString(id), entity);
                ListCourseResponseDto response = courseMapper.toListResponseDto(updatedCourse);
                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<ListCourseResponseDto>> getAllCourses(
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(required = false) String category_id) {
                List<Course> courses = courseService.getAllCourses(name, category_id);
                List<ListCourseResponseDto> response = courses.stream()
                                .map(courseMapper::toListResponseDto)
                                .toList();
                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
                courseService.deleteCourse(UUID.fromString(id));
                return ResponseEntity.noContent().build();
        }

        @PatchMapping("/{id}/active")
        @Operation(summary = "Ativar/Desativar curso", description = "Endpoint para ativar ou desativar um curso. Requer o id do curso.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Curso ativado/desativado com sucesso"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Requisição inválida, como curso não encontrado")
        })
        public ResponseEntity<Void> toggleCourseActive(@PathVariable String id) {
                courseService.tooggleActive(UUID.fromString(id));
                return ResponseEntity.noContent().build();
        }
}
