package com.jonathastassi.api_courses_ti.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

    public List<User> findAllByNameContainingIgnoreCase(String name);

    public Optional<User> findByIdAndRole(UUID id, UserRoleEnum role);
}
