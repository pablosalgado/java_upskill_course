package com.epam.pablo.service.impl;

import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.UserImpl;
import com.epam.pablo.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private Storage storage;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User user = new UserImpl();
        user.setId(userId);
        when(storage.getById(User.class, userId)).thenReturn(user);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(storage).getById(User.class, userId);
    }

    @Test
    void testGetUserByEmail() {
        String email = "user@example.com";
        User user = new UserImpl();
        user.setEmail(email);
        when(storage.selectAll(User.class)).thenReturn(List.of(user));

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(storage).selectAll(User.class);
    }

    @Test
    void testGetUsersByName() {
        String name = "John";
        int pageSize = 2;
        int pageNum = 0;
        List<User> users = IntStream.range(0, 5)
                .mapToObj(i -> {
                    User user = new UserImpl();
                    user.setName(name);
                    return user;
                })
                .collect(Collectors.toList());
        when(storage.selectAll(User.class)).thenReturn(users);

        List<User> result = userService.getUsersByName(name, pageSize, pageNum);

        assertEquals(pageSize, result.size());
        assertTrue(result.stream().allMatch(u -> u.getName().equals(name)));
        verify(storage).selectAll(User.class);
    }

    @Test
    void testCreateUser() {
        User user = new UserImpl();
        when(storage.save(user, User.class)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        verify(storage).save(user, User.class);
    }

    @Test
    void testUpdateUser() {
        User user = new UserImpl();
        when(storage.update(user, User.class)).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        verify(storage).update(user, User.class);
    }

    @Test
    void testDeleteUser() {
        long userId = 1L;
        when(storage.delete(User.class, userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(storage).delete(User.class, userId);
    }
}