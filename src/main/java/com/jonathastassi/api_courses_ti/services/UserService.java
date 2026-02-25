package com.jonathastassi.api_courses_ti.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.exceptions.EmailAlreadyExistsException;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException();
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<User> listUsers(String name) {
        if (name != null && !name.isEmpty()) {
            return userRepository.findAllByNameContainingIgnoreCase(name);
        }
        return userRepository.findAll();
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
