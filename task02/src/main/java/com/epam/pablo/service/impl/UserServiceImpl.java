package com.epam.pablo.service.impl;

import com.epam.pablo.storage.Storage;
import com.epam.pablo.entity.User;
import com.epam.pablo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final Storage storage;

    @Autowired
    public UserServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public User getUserById(long userId) {
        return (User) storage.getById(User.class, userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) storage.selectAll(User.class)
                .stream()
                .filter(user -> ((User)user).getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return storage.selectAll(User.class)
                .stream()
                .filter(user -> ((User)user).getName().equals(name))
                .skip(pageSize * pageNum)
                .limit(pageSize)
                .toList();
    }

    @Override
    public User createUser(User user) {
        return (User) storage.save(user, User.class);
    }

    @Override
    public User updateUser(User user) {
        return (User) storage.update(user, User.class);
    }

    @Override
    public boolean deleteUser(long userId) {
        return storage.delete(User.class, userId);
    }
}