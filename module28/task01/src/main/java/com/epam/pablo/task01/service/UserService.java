package com.epam.pablo.task01.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.epam.pablo.task01.model.User;

public interface UserService {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId the unique identifier of the user to be retrieved
     * @return the user associated with the specified identifier
     */
    User getUserById(long userId);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return the user associated with the given email address, or null if no such user exists
     */
    User getUserByEmail(String email);

    /**
     * Retrieves a paginated list of users filtered by their name.
     *
     * @param name the name to filter users by
     * @param pageSize the number of users to return per page
     * @param pageNum the page number to retrieve
     * @return a list of users that match the specified name
     */
    List<User> getUsersByName(String name, int pageSize, int pageNum);

    /**
     * Creates a new user.
     *
     * @param user the user to be created
     * @return the created user
     */
    User createUser(User user);

    /**
     * Updates the user with the specified ID.
     *
     * @param id the ID of the user to be updated
     * @param user the user object containing updated information
     * @return the updated user object
     */
    User updateUser(Long id, User user);

    /**
     * Deletes a user with the specified user ID.
     *
     * @param userId the ID of the user to be deleted
     * @return true if the user was successfully deleted, false otherwise
     */
    boolean deleteUser(long userId);

    /**
     * Refills the account of the user with the specified amount.
     *
     * @param userId the ID of the user whose account is to be refilled
     * @param amount the amount to be added to the user's account
     */
    void refillAccount(long userId, BigDecimal amount);

    /**
     * Retrieves a paginated list of all users.
     *
     * @param newParam an integer parameter for additional functionality (describe its purpose).
     * @param pageNum the page number to retrieve.
     * @return a Page object containing User entities.
     */
    Page<User> getAllUsers(int newParam, int pageNum);

}
