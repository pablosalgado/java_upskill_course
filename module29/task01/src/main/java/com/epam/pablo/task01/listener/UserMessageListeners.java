package com.epam.pablo.task01.listener;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("async")
public class UserMessageListeners {

    Logger logger = LoggerFactory.getLogger(UserMessageListeners.class);

    private final BookingFacade bookingFacade;
    private final ObjectMapper objectMapper;

    public UserMessageListeners(BookingFacade bookingFacade, ObjectMapper objectMapper) {
        this.bookingFacade = bookingFacade;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "createUserQueue")
    public void onCreateUser(String message) {
        logger.debug("Received message for createUserQueue: " + message);
        try {
            User user = objectMapper.readValue(message, User.class);
            bookingFacade.createUser(user);
            logger.info("Successfully processed createUserQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

    @JmsListener(destination = "updateUserQueue")
    public void onUpdateUser(Object[] message) {
        logger.debug("Received message for updateUserQueue: " + message);
        try {
            Long id = objectMapper.readValue(message[0].toString(), Long.class);
            User user = objectMapper.readValue(message[1].toString(), User.class);
            bookingFacade.updateUser(id, user);
            logger.info("Successfully processed updateUserQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

    @JmsListener(destination = "deleteUserQueue")
    public void onDeleteUser(String message) {
        logger.debug("Received message for deleteUserQueue: " + message);
        try {
            Long userId = objectMapper.readValue(message, Long.class);
            bookingFacade.deleteUser(userId);
            logger.info("Successfully processed deleteUserQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }
    
}