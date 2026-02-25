package com.jonathastassi.api_courses_ti.initializers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
    }

    @Transactional
    private void initializeUsers() {
        String adminEmail = "admin@admin.com";

        try {
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .name("Administrator")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin"))
                        .role(UserRoleEnum.ADMIN)
                        .build();

                userRepository.saveAndFlush(admin);
                System.out.println("✓ Usuário admin criado com sucesso!");
            } else {
                System.out.println("✓ Usuário admin já existe!");
            }
        } catch (Exception e) {
            System.err.println("✗ Erro ao inicializar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
