package com.epam.pablo.integration;

import com.epam.pablo.entity.Event;
import com.epam.pablo.entity.Ticket;
import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.EventImpl;
import com.epam.pablo.entity.impl.UserImpl;
import com.epam.pablo.facade.BookingFacade;
import com.epam.pablo.service.impl.exception.EventNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void testBookingScenario() {
        var user = bookingFacade.getUserById(1L);
        assertEquals("John Doe", user.getName());

        // Create an event
        Event event = new EventImpl();
        event.setTitle("Concert");
        event.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(event);
        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getId());

        // Book a ticket
        Ticket ticket = bookingFacade.bookTicket(user.getId(), createdEvent.getId(), 1, Ticket.Category.PREMIUM);
        assertNotNull(ticket);
        assertNotNull(ticket.getId());

        // Cancel the ticket
        boolean cancelStatus = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(cancelStatus);
    }

    @Test
    public void testBookingNonExistentEvent() {
        var user = new UserImpl();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        User createdUser = bookingFacade.createUser(user);
        assertNotNull(createdUser);

        assertThrows(EventNotFoundException.class, () -> {
            bookingFacade.bookTicket(createdUser.getId(), 999L, 1, Ticket.Category.PREMIUM);
        });
    }

    @Test
    public void testUpdateUser() {
        User user = new UserImpl();
        user.setName("Alice Smith");
        user.setEmail("alice.smith@example.com");
        bookingFacade.createUser(user);

        var result = bookingFacade.getUserById(5L);
        assertEquals("Alice Smith", result.getName());

        result.setName("John Johnson");
        bookingFacade.updateUser(result);

        result = bookingFacade.getUserById(5L);
        assertEquals("John Johnson", result.getName());
    }

    @Test
    public void testUserRetrievalAndPagination() {
        var users = bookingFacade.getUsersByName("John", 10, 0);
        assertNotNull(users);
        assertTrue(users.size() <= 10);
    }

    @Test
    public void testEventRetrievalAndPagination() {
        var events = bookingFacade.getEventsByTitle("Concert", 10, 0);
        assertNotNull(events);
        assertTrue(events.size() <= 10);
    }
}