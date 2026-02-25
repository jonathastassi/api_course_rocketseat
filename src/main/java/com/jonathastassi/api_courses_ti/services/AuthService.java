package com.jonathastassi.api_courses_ti.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jonathastassi.api_courses_ti.dtos.LoginRequestDto;
import com.jonathastassi.api_courses_ti.dtos.LoginResponseDto;
import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.exceptions.InvalidEmailOrPasswordException;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new InvalidEmailOrPasswordException());

        if (!passwordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().toString())
                .withExpiresAt(Instant.now().plusMillis(expiration))
                .sign(algorithm);

        return new LoginResponseDto(token);
    }
}
