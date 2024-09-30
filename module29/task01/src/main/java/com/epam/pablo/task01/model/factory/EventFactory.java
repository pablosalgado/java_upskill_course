package com.epam.pablo.task01.model.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.epam.pablo.task01.model.Event;

@Component
public class EventFactory {

    public Event createEvent(String title, LocalDateTime date) {
        var event = new Event();
        event.setTitle(title);
        event.setDate(date);
        return event;
    }

    public Event createEvent(String title, LocalDateTime date,  BigDecimal ticketPrice) {
        var event = new Event();
        event.setTitle(title);
        event.setDate(date);
        event.setTicketPrice(ticketPrice);
        return event;
    }
}
