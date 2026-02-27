package com.jonathastassi.api_courses_ti.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jonathastassi.api_courses_ti.entities.User;
import com.jonathastassi.api_courses_ti.enums.UserRoleEnum;
import com.jonathastassi.api_courses_ti.exceptions.EmailAlreadyExistsException;
import com.jonathastassi.api_courses_ti.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private User existingUser;
    private String rawPassword;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        rawPassword = "password123";
        encodedPassword = "encodedPassword123";

        user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .password(rawPassword)
                .role(UserRoleEnum.STUDENT)
                .build();

        existingUser = User.builder()
                .id(UUID.randomUUID())
                .name("Jane Smith")
                .email("john.doe@example.com")
                .password("existingPassword")
                .role(UserRoleEnum.TEACHER)
                .build();
    }

    @Test
    @DisplayName("Should create user successfully")
    void createUser_Success() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        
        User savedUser = User.builder()
                .id(userId)
                .name(user.getName())
                .email(user.getEmail())
                .password(encodedPassword)
                .role(user.getRole())
                .build();
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(user.getRole(), result.getRole());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void createUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createUser(user));

        assertNotNull(exception);
        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should encode password before saving user")
    void createUser_PasswordEncoded() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.createUser(user);

        // Assert
        assertEquals(encodedPassword, user.getPassword());
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Should list all users when name is null")
    void listUsers_NameIsNull_ReturnAllUsers() {
        // Arrange
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .role(UserRoleEnum.TEACHER)
                .build();

        List<User> allUsers = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(allUsers);

        // Act
        List<User> result = userService.listUsers(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(user));
        assertTrue(result.contains(user2));

        verify(userRepository).findAll();
        verify(userRepository, never()).findAllByNameContainingIgnoreCase(any());
    }

    @Test
    @DisplayName("Should list all users when name is empty")
    void listUsers_NameIsEmpty_ReturnAllUsers() {
        // Arrange
        List<User> allUsers = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(allUsers);

        // Act
        List<User> result = userService.listUsers("");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));

        verify(userRepository).findAll();
        verify(userRepository, never()).findAllByNameContainingIgnoreCase(any());
    }

    @Test
    @DisplayName("Should list users filtered by name when name is provided")
    void listUsers_NameProvided_ReturnFilteredUsers() {
        // Arrange
        String searchName = "John";
        List<User> filteredUsers = Arrays.asList(user);
        when(userRepository.findAllByNameContainingIgnoreCase(searchName)).thenReturn(filteredUsers);

        // Act
        List<User> result = userService.listUsers(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));

        verify(userRepository).findAllByNameContainingIgnoreCase(searchName);
        verify(userRepository, never()).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no users match the name filter")
    void listUsers_NoUsersMatchName_ReturnEmptyList() {
        // Arrange
        String searchName = "NonExistentName";
        when(userRepository.findAllByNameContainingIgnoreCase(searchName)).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.listUsers(searchName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository).findAllByNameContainingIgnoreCase(searchName);
        verify(userRepository, never()).findAll();
    }

    @Test
    @DisplayName("Should list users with case insensitive name search")
    void listUsers_CaseInsensitiveSearch() {
        // Arrange
        String searchName = "JOHN";
        List<User> filteredUsers = Arrays.asList(user);
        when(userRepository.findAllByNameContainingIgnoreCase(searchName)).thenReturn(filteredUsers);

        // Act
        List<User> result = userService.listUsers(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));

        verify(userRepository).findAllByNameContainingIgnoreCase(searchName);
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteUser_Success() {
        // Arrange
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should call deleteById even if user does not exist")
    void deleteUser_UserNotExists_StillCallsDelete() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(nonExistentId);

        // Act
        userService.deleteUser(nonExistentId);

        // Assert
        verify(userRepository).deleteById(nonExistentId);
    }

    @Test
    @DisplayName("Should handle repository exception during user deletion")
    void deleteUser_RepositoryException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(userId);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(userId));

        assertEquals("Database error", exception.getMessage());
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should handle multiple users with similar names in search")
    void listUsers_MultipleUsersWithSimilarNames() {
        // Arrange
        User johnDoe = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .role(UserRoleEnum.STUDENT)
                .build();

        User johnSmith = User.builder()
                .id(UUID.randomUUID())
                .name("John Smith")
                .email("john.smith@example.com")
                .role(UserRoleEnum.TEACHER)
                .build();

        String searchName = "John";
        List<User> filteredUsers = Arrays.asList(johnDoe, johnSmith);
        when(userRepository.findAllByNameContainingIgnoreCase(searchName)).thenReturn(filteredUsers);

        // Act
        List<User> result = userService.listUsers(searchName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(johnDoe));
        assertTrue(result.contains(johnSmith));

        verify(userRepository).findAllByNameContainingIgnoreCase(searchName);
    }
}
