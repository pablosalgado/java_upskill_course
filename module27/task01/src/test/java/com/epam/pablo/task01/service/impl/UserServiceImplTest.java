package com.epam.pablo.task01.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.UserRepository;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1L);
        verify(userRepository).findById(1L);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        User result = userService.getUserByEmail("test@example.com");
        verify(userRepository).findByEmail("test@example.com");
        assertEquals(user, result);
    }

    @Test
    public void testGetUsersByName() {
        List<User> users = Arrays.asList(new User(), new User());
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findByName(eq("John"), any())).thenReturn(page);
        List<User> result = userService.getUsersByName("John", 10, 1);
        verify(userRepository).findByName(eq("John"), any(PageRequest.class));
        assertEquals(users, result);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.createUser(user);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.updateUser(user);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testUpdateUserNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(false);
        User result = userService.updateUser(user);
        assertNull(result);
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        boolean result = userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
        assertTrue(result);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        boolean result = userService.deleteUser(1L);
        assertFalse(result);
    }

    @Test
    public void testRefillAccount() {
        long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.refillAccount(userId, BigDecimal.TEN);

        verify(userRepository).save(user);
        assertEquals(BigDecimal.valueOf(10), user.getAccountBalance());
    }
}