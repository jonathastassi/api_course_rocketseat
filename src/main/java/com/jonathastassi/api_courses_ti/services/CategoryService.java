package com.jonathastassi.api_courses_ti.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonathastassi.api_courses_ti.entities.Category;
import com.jonathastassi.api_courses_ti.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId).map(category -> {
            category.setName(updatedCategory.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategoryById(UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
