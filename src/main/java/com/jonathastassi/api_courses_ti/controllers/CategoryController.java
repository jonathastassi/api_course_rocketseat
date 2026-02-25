package com.jonathastassi.api_courses_ti.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathastassi.api_courses_ti.entities.Category;
import com.jonathastassi.api_courses_ti.services.CategoryService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> listCategories() {
        List<Category> categories = categoryService.listCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category entity) {
        Category createdCategory = categoryService.createCategory(entity);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category entity) {
        Category updatedCategory = categoryService.updateCategory(UUID.fromString(id), entity);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategoryById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }   
}
