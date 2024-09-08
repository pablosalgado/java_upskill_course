package com.epam.pablo.task05.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.pablo.task05.model.User;

@SpringBootTest
public class UserDaoTests {

    @Autowired
    private UserDao userDao;

    @MockBean
    private CommandLineRunner runner;

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setName("John");
        newUser.setSurname("Doe");
        newUser.setBirthdate(Date.valueOf("1990-01-01"));

        User createdUser = userDao.create(newUser);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("John");
        assertThat(createdUser.getSurname()).isEqualTo("Doe");
        assertThat(createdUser.getBirthdate()).isEqualTo(Date.valueOf("1990-01-01"));
    }

    @Test
    public void testFindById() {
        User user = userDao.findById(1);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    public void testFindAll() {
        List<User> users = userDao.findAll();
        assertThat(users).isNotEmpty();
    }

    @Test
    public void testUpdateUser() {
        User user = userDao.findById(1);
        user.setName("Jane");
        User updatedUser = userDao.update(user);
        assertThat(updatedUser.getName()).isEqualTo("Jane");
    }

    @Test
    public void testDeleteUser() {
        // Create a user to delete
        User newUser = new User();
        newUser.setName("Temp");
        newUser.setSurname("User");
        newUser.setBirthdate(Date.valueOf("1990-01-01"));
        User createdUser = userDao.create(newUser);

        // Delete the user
        userDao.delete(createdUser.getId());

        // Verify user is deleted
        User deletedUser = userDao.findById(createdUser.getId());
        assertThat(deletedUser).isNull();
    }
}