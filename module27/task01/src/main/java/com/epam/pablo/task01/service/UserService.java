package com.epam.pablo.task01.service;

import java.math.BigDecimal;
import java.util.List;

import com.epam.pablo.task01.model.User;

public interface UserService {

    User getUserById(long userId);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    void refillAccount(long userId, BigDecimal amount);

}
