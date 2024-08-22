package com.epam.pablo.facade.impl;

import com.epam.pablo.entity.Event;
import com.epam.pablo.entity.Ticket;
import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.EventImpl;
import com.epam.pablo.entity.impl.TicketImpl;
import com.epam.pablo.entity.impl.UserImpl;
import com.epam.pablo.service.EventService;
import com.epam.pablo.service.TicketService;
import com.epam.pablo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BookingFacadeImplTest {
    @Mock
    private UserService userService;
    @Mock
    private EventService eventService;
    @Mock
    private TicketService ticketService;

    @InjectMocks
    private BookingFacadeImpl bookingFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventById() {
        long eventId = 1L;
        Event event = new EventImpl();
        when(eventService.getEventById(eventId)).thenReturn(event);

        Event result = bookingFacade.getEventById(eventId);

        verify(eventService).getEventById(eventId);
        assertSame(event, result);
    }

    @Test
    void testGetEventsByTitle() {
        String title = "Concert";
        int pageSize = 10;
        int pageNum = 0;
        List<Event> events = List.of(new EventImpl());
        when(eventService.getEventsByTitle(title, pageSize, pageNum)).thenReturn(events);

        List<Event> result = bookingFacade.getEventsByTitle(title, pageSize, pageNum);

        verify(eventService).getEventsByTitle(title, pageSize, pageNum);
        assertSame(events, result);
    }

    @Test
    void testGetEventsForDay() {
        Date day = new Date();
        int pageSize = 10;
        int pageNum = 0;
        List<Event> events = List.of(new EventImpl());
        when(eventService.getEventsForDay(day, pageSize, pageNum)).thenReturn(events);

        List<Event> result = bookingFacade.getEventsForDay(day, pageSize, pageNum);

        verify(eventService).getEventsForDay(day, pageSize, pageNum);
        assertSame(events, result);
    }

    @Test
    void testCreateEvent() {
        Event event = new EventImpl();
        when(eventService.createEvent(event)).thenReturn(event);

        Event result = bookingFacade.createEvent(event);

        verify(eventService).createEvent(event);
        assertSame(event, result);
    }

    @Test
    void testUpdateEvent() {
        Event event = new EventImpl();
        when(eventService.updateEvent(event)).thenReturn(event);

        Event result = bookingFacade.updateEvent(event);

        verify(eventService).updateEvent(event);
        assertSame(event, result);
    }

    @Test
    void testDeleteEvent() {
        long eventId = 1L;
        when(eventService.deleteEvent(eventId)).thenReturn(true);

        boolean result = bookingFacade.deleteEvent(eventId);

        verify(eventService).deleteEvent(eventId);
        assertTrue(result);
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User user = new UserImpl();
        when(userService.getUserById(userId)).thenReturn(user);

        User result = bookingFacade.getUserById(userId);

        verify(userService).getUserById(userId);
        assertSame(user, result);
    }

    @Test
    void testGetUserByEmail() {
        String email = "user@example.com";
        User user = new UserImpl();
        when(userService.getUserByEmail(email)).thenReturn(user);

        User result = bookingFacade.getUserByEmail(email);

        verify(userService).getUserByEmail(email);
        assertSame(user, result);
    }

    @Test
    void testGetUsersByName() {
        String name = "John";
        int pageSize = 10;
        int pageNum = 0;
        List<User> users = List.of(new UserImpl());
        when(userService.getUsersByName(name, pageSize, pageNum)).thenReturn(users);

        List<User> result = bookingFacade.getUsersByName(name, pageSize, pageNum);

        verify(userService).getUsersByName(name, pageSize, pageNum);
        assertSame(users, result);
    }

    @Test
    void testCreateUser() {
        User user = new UserImpl();
        when(userService.createUser(user)).thenReturn(user);

        User result = bookingFacade.createUser(user);

        verify(userService).createUser(user);
        assertSame(user, result);
    }

    @Test
    void testUpdateUser() {
        User user = new UserImpl();
        when(userService.updateUser(user)).thenReturn(user);

        User result = bookingFacade.updateUser(user);

        verify(userService).updateUser(user);
        assertSame(user, result);
    }

    @Test
    void testDeleteUser() {
        long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(true);

        boolean result = bookingFacade.deleteUser(userId);

        verify(userService).deleteUser(userId);
        assertTrue(result);
    }

    @Test
    void testBookTicket() {
        long userId = 1L;
        long eventId = 2L;
        int place = 10;
        Ticket.Category category = Ticket.Category.STANDARD;
        Ticket ticket = new TicketImpl();
        when(ticketService.bookTicket(userId, eventId, place, category)).thenReturn(ticket);

        Ticket result = bookingFacade.bookTicket(userId, eventId, place, category);

        verify(ticketService).bookTicket(userId, eventId, place, category);
        assertSame(ticket, result);
    }

    @Test
    void testGetBookedTicketsByUser() {
        User user = new UserImpl();
        int pageSize = 10;
        int pageNum = 0;
        List<Ticket> tickets = List.of(new TicketImpl());
        when(ticketService.getBookedTickets(user, pageSize, pageNum)).thenReturn(tickets);

        List<Ticket> result = bookingFacade.getBookedTickets(user, pageSize, pageNum);

        verify(ticketService).getBookedTickets(user, pageSize, pageNum);
        assertSame(tickets, result);
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event event = new EventImpl();
        int pageSize = 10;
        int pageNum = 0;
        List<Ticket> tickets = List.of(new TicketImpl());
        when(ticketService.getBookedTickets(event, pageSize, pageNum)).thenReturn(tickets);

        List<Ticket> result = bookingFacade.getBookedTickets(event, pageSize, pageNum);

        verify(ticketService).getBookedTickets(event, pageSize, pageNum);
        assertSame(tickets, result);
    }

    @Test
    void testCancelTicket() {
        long ticketId = 1L;
        when(ticketService.cancelTicket(ticketId)).thenReturn(true);

        boolean result = bookingFacade.cancelTicket(ticketId);

        verify(ticketService).cancelTicket(ticketId);
        assertTrue(result);
    }
}