package com.epam.pablo.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(Long eventId) {
        super("Event with id " + eventId + " not found");
    }

}
