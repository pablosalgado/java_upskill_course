package com.epam.pablo.task01.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }

}
