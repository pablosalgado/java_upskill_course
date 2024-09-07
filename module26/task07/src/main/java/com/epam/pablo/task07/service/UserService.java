package com.epam.pablo.task07.service;

import java.util.List;

import com.epam.pablo.task07.model.User;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    boolean create(User user);

    boolean update(User user);

    boolean delete(int id);
}
