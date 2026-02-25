package com.jonathastassi.api_courses_ti.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathastassi.api_courses_ti.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
