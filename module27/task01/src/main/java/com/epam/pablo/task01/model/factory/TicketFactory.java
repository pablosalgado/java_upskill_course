package com.epam.pablo.task01.model.factory;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import org.springframework.stereotype.Component;

@Component
public class TicketFactory {

    public Ticket createTicket(User user, Event event, int place, Ticket.Category category) {
        var ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setPlace(place);
        ticket.setCategory(category);
        return ticket;
    }

}
