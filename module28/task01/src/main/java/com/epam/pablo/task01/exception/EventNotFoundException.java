package com.epam.pablo.task01.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(Long eventId) {
        super("Event with id " + eventId + " not found");
    }

}
