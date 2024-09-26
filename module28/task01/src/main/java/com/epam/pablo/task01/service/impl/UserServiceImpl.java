package com.epam.pablo.task01.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.pablo.task01.exception.UserNotFoundException;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.UserRepository;
import com.epam.pablo.task01.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long userId) {
        logger.debug("Fetching user by ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        logger.debug("Fetching users by name: {}, page size: {}, page number: {}", name, pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userRepository.findByName(name, pageable).getContent();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        logger.debug("Creating new user: {}", user);
        User savedUser = userRepository.save(user);
        logger.debug("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        logger.debug("Updating user with ID: {}", id);

        var userToUpdate = userRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempted to update non-existing user with ID: {}", id);
            return new UserNotFoundException(id);
        });
        
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());

        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) {
        logger.debug("Deleting user with ID: {}", userId);
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            logger.debug("User deleted successfully with ID: {}", userId);
            return true;
        }
        logger.warn("Attempted to delete non-existing user with ID: {}", userId);
        return false;
    }

    @Override
    public void refillAccount(long userId, BigDecimal amount) {
        logger.debug("Refilling account for user ID: {} with amount: {}", userId, amount);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User with ID: {} not found", userId);
            throw  new UserNotFoundException(userId);
        }

        user.addFundsToAccount(amount);
        userRepository.save(user);

        logger.debug("Account refilled successfully for user ID: {}", userId);
    }

    @Override
    public Page<User> getAllUsers(int pageSize, int pageNum) {
        logger.debug("Fetching all users");
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return userRepository.findAllWithUserAccount(pageable);
    }
}