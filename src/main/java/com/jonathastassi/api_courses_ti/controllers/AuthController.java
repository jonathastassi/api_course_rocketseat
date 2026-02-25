package com.jonathastassi.api_courses_ti.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathastassi.api_courses_ti.dtos.LoginRequestDto;
import com.jonathastassi.api_courses_ti.dtos.LoginResponseDto;
import com.jonathastassi.api_courses_ti.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.authenticate(loginRequestDto);
    }

}
