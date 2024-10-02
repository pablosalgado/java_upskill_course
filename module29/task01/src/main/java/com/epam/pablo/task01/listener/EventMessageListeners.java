package com.epam.pablo.task01.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EventMessageListeners {

    Logger logger = LoggerFactory.getLogger(EventMessageListeners.class);

    private final BookingFacade bookingFacade;
    private final ObjectMapper objectMapper;

    public EventMessageListeners(BookingFacade bookingFacade, ObjectMapper objectMapper) {
        this.bookingFacade = bookingFacade;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "createEventQueue")
    public void onCreateEvent(String message) {
        logger.debug("Received message for createEventQueue: " + message);
        try {
            Event event = objectMapper.readValue(message, Event.class);
            bookingFacade.createEvent(event);
            logger.info("Successfully processed createEventQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

    @JmsListener(destination = "updateEventQueue")
    public void onUpdateEvent(Object[] message) {
        logger.debug("Received message for updateEventQueue: " + message);
        try {
            Long id = objectMapper.readValue(message[0].toString(), Long.class);
            Event event = objectMapper.readValue(message[1].toString(), Event.class);
            bookingFacade.updateEvent(id, event);
            logger.info("Successfully processed updateEventQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

    @JmsListener(destination = "deleteEventQueue")
    public void onDeleteEvent(String message) {
        logger.debug("Received message for deleteEventQueue: " + message);
        try {
            Long eventId = objectMapper.readValue(message, Long.class);
            bookingFacade.deleteEvent(eventId);
            logger.info("Successfully processed deleteEventQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }
    
}