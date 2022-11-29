package com.krech.botv3.service;

import com.krech.botv3.domain.User;
import com.krech.botv3.domain.rest.request.CreateUserRequest;
import com.krech.botv3.domain.rest.request.UpdateUserRequest;
import com.krech.botv3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserService userService;


    @BeforeEach
    public void init() {
        userService = new UserService(userRepository, bCryptPasswordEncoder);
    }

    @Test
    void createTest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setLogin("Феникс");
        request.setPassword("абыр");
        request.setName("Петя");
        request.setSurname("Иванов");
        request.setRole("user");

        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("абыр2");

        userService.create(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        assertEquals(request.getLogin(), captor.getValue().getLogin());
        assertEquals(request.getName(), captor.getValue().getName());
        assertEquals(request.getSurname(), captor.getValue().getSurname());
        assertEquals(request.getRole(), captor.getValue().getRoleStr());
        assertEquals("абыр2", captor.getValue().getPassword());
    }

    @Test
    void createTestOfException() {
        CreateUserRequest request = new CreateUserRequest();
        request.setLogin("Феникс");
        request.setPassword("абыр");
        request.setName("Петя");
        request.setSurname("Иванов");
        request.setRole("user");

        User existingUser = new User();
        existingUser.setPassword("абыр");
        existingUser.setName("Петя");
        existingUser.setSurname("Иванов");
        existingUser.setRole(1);
        existingUser.setId(1);

        when(userRepository.findByLogin(request.getLogin())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> userService.create(request));

    }

    @Test
    void updateUserTest() {
        Long userId = 1L;

        UpdateUserRequest request = new UpdateUserRequest();
        request.setPassword("абыр2");
        request.setName("Петя");
        request.setSurname("Иванов");
        request.setRole("user");

        User existingUser = new User();
        existingUser.setPassword("абыр1");
        existingUser.setName("Вася");
        existingUser.setSurname("Петров");
        existingUser.setRole(1);
        existingUser.setId(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("абыр2");

        userService.updateUser(userId, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        assertEquals("Петя", captor.getValue().getName());
        assertEquals("Иванов", captor.getValue().getSurname());
        assertEquals("user", captor.getValue().getRoleStr());
        assertEquals("абыр2", captor.getValue().getPassword());
    }

    @Test
    void updateUserTestOfException() {
        Long userId = 1L;

        UpdateUserRequest request = new UpdateUserRequest();
        request.setPassword("абыр2");
        request.setName("Петя");
        request.setSurname("Иванов");
        request.setRole("user");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, request));
    }

    @Test
    void findByLoginAndVerifyPasswordTest() {
        String login = "Petya";
        String password = "абыр";

        User existingUser = new User();
        existingUser.setPassword("абыр");
        existingUser.setName("Петя");
        existingUser.setSurname("Иванов");
        existingUser.setRole(1);
        existingUser.setId(1);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.matches(password, "абыр")).thenReturn(true);

        User result = userService.findByLoginAndVerifyPassword(login, password);

        assertEquals("Петя", result.getName());
        assertEquals("Иванов", result.getSurname());
        assertEquals("user", result.getRoleStr());
        assertEquals("абыр", result.getPassword());
    }

    @Test
    void findByLoginAndVerifyPasswordTestOfException1() {
        String login = "Petya";
        String password = "абыр1";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(AuthenticationServiceException.class, () -> userService.findByLoginAndVerifyPassword(login, password));
    }

    @Test
    void findByLoginAndVerifyPasswordTestOfException2() {
        String login = "Petya";
        String password = "абыр1";

        User existingUser = new User();
        existingUser.setPassword("абыр");
        existingUser.setName("Петя");
        existingUser.setSurname("Иванов");
        existingUser.setRole(1);
        existingUser.setId(1);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.matches(password, "абыр")).thenReturn(false);

        assertThrows(AuthenticationServiceException.class, () -> userService.findByLoginAndVerifyPassword(login, password));
    }

}