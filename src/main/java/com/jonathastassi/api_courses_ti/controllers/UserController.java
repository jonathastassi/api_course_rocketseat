package com.jonathastassi.api_courses_ti.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jonathastassi.api_courses_ti.dtos.CreateUserRequestDto;
import com.jonathastassi.api_courses_ti.dtos.CreateUserResponseDto;
import com.jonathastassi.api_courses_ti.dtos.ListUserResponseDto;
import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;
import com.jonathastassi.api_courses_ti.exceptions.EmailAlreadyExistsException;
import com.jonathastassi.api_courses_ti.mappers.UserMapper;
import com.jonathastassi.api_courses_ti.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = userMapper.toEntity(createUserRequestDto);
        CreateUserResponseDto createdUser = userMapper.toResponseDto(userService.createUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<ListUserResponseDto>> listUsers(
            @RequestParam(value = "name", required = false) String name) {
        List<User> users = userService.listUsers(name);
        List<ListUserResponseDto> response = users.stream().map(user -> userMapper.toResponseListDto(user)).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
