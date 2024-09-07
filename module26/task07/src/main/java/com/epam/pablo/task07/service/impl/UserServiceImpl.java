package com.epam.pablo.task07.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epam.pablo.task07.dao.UserDao;
import com.epam.pablo.task07.model.User;
import com.epam.pablo.task07.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public User getById(int userId) {
        return userDao.findById(userId);
    }

    @Override
    public boolean create(User user) {
        return userDao.create(user) > 0;
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user) > 0;
    }

    @Override
    public boolean delete(int userId) {
        return userDao.delete(userId) > 0;
    }
}
