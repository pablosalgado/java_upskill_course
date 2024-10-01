package com.epam.pablo.task01.facade.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.service.EventService;
import com.epam.pablo.task01.service.TicketService;
import com.epam.pablo.task01.service.UserService;

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
        Event event = new Event();
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
        List<Event> events = List.of(new Event());
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
        List<Event> events = List.of(new Event());
        when(eventService.getEventsForDay(day, pageSize, pageNum)).thenReturn(events);

        List<Event> result = bookingFacade.getEventsForDay(day, pageSize, pageNum);

        verify(eventService).getEventsForDay(day, pageSize, pageNum);
        assertSame(events, result);
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        when(eventService.createEvent(event)).thenReturn(event);

        Event result = bookingFacade.createEvent(event);

        verify(eventService).createEvent(event);
        assertSame(event, result);
    }

    @Test
    void testUpdateEvent() {
        Event eventToUpdate = new Event();
        Event updatedEvent = new Event();
        when(eventService.updateEvent(eventToUpdate.getId(), eventToUpdate)).thenReturn(updatedEvent);

        Event result = bookingFacade.updateEvent(eventToUpdate.getId(), eventToUpdate);

        verify(eventService).updateEvent(eventToUpdate.getId(), eventToUpdate);
        assertSame(updatedEvent, result);
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
        User user = new User();
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        User result = bookingFacade.getUserById(userId).get();

        verify(userService).getUserById(userId);
        assertSame(user, result);
    }

    @Test
    void testGetUserByEmail() {
        String email = "user@example.com";
        User user = new User();
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
        List<User> users = List.of(new User());
        when(userService.getUsersByName(name, pageSize, pageNum)).thenReturn(users);

        List<User> result = bookingFacade.getUsersByName(name, pageSize, pageNum);

        verify(userService).getUsersByName(name, pageSize, pageNum);
        assertSame(users, result);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);

        User result = bookingFacade.createUser(user);

        verify(userService).createUser(user);
        assertSame(user, result);
    }

    @Test
    void testUpdateUser() {
        var userToUpdate = new User();
        var updatedUser = new User();

        when(userService.updateUser(userToUpdate.getId(), userToUpdate)).thenReturn(updatedUser);

        User result = bookingFacade.updateUser(userToUpdate.getId(), userToUpdate);

        verify(userService).updateUser(userToUpdate.getId(), userToUpdate);
        assertSame(updatedUser, result);
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
        Ticket ticket = new Ticket();
        when(ticketService.bookTicket(userId, eventId, place, category)).thenReturn(ticket);

        Ticket result = bookingFacade.bookTicket(userId, eventId, place, category);

        verify(ticketService).bookTicket(userId, eventId, place, category);
        assertSame(ticket, result);
    }

    @Test
    void testGetBookedTicketsByUser() {
        User user = new User();
        int pageSize = 10;
        int pageNum = 0;
        List<Ticket> tickets = List.of(new Ticket());
        when(ticketService.getBookedTickets(user, pageSize, pageNum)).thenReturn(tickets);

        List<Ticket> result = bookingFacade.getBookedTickets(user, pageSize, pageNum);

        verify(ticketService).getBookedTickets(user, pageSize, pageNum);
        assertSame(tickets, result);
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event event = new Event();
        int pageSize = 10;
        int pageNum = 0;
        List<Ticket> tickets = List.of(new Ticket());
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

    @Test
    void testRefillAccount() {
        long userId = 1L;
        var amount = BigDecimal.TEN;

        bookingFacade.refillAccount(userId, amount);

        verify(userService).refillAccount(userId, amount);
    }
}