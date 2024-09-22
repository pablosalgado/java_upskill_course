package com.epam.pablo.task01.model.factory;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketFactoryTest {

    private TicketFactory factory = new TicketFactory();

    @Test
    public void testCreateTicket() {
        User user = new User();
        Event event = new Event();
        int place = 10;
        Ticket.Category category = Ticket.Category.PREMIUM;
        
        Ticket ticket = factory.createTicket(user, event, place, category);
        
        assertNotNull(ticket);
        assertSame(user, ticket.getUser());
        assertSame(event, ticket.getEvent());
        assertEquals(10, ticket.getPlace());
        assertEquals(Ticket.Category.PREMIUM, ticket.getCategory());
    }   

}