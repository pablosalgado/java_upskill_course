package com.epam.pablo.service.impl;


import com.epam.pablo.entity.Event;
import com.epam.pablo.entity.Ticket;
import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.EventImpl;
import com.epam.pablo.entity.impl.TicketImpl;
import com.epam.pablo.entity.impl.UserImpl;
import com.epam.pablo.service.impl.exception.EventNotFoundException;
import com.epam.pablo.service.impl.exception.UserNotFoundException;
import com.epam.pablo.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {
    @Mock
    private Storage storage;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTicket() {
        long userId = 1L;
        long eventId = 100L;
        int place = 10;
        Ticket.Category category = Ticket.Category.STANDARD;

        User user = mock(User.class);
        Event event = mock(Event.class);
        Ticket ticket = new TicketImpl();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setPlace(place);
        ticket.setCategory(category);

        when(user.getId()).thenReturn(userId);
        when(event.getId()).thenReturn(eventId);
        when(storage.getById(User.class, userId)).thenReturn(user);
        when(storage.getById(Event.class, eventId)).thenReturn(event);
        when(storage.save(any(Ticket.class), eq(Ticket.class))).thenReturn(ticket);

        Ticket bookedTicket = ticketService.bookTicket(userId, eventId, place, category);

        assertNotNull(bookedTicket);
        assertEquals(userId, bookedTicket.getUserId());
        assertEquals(eventId, bookedTicket.getEventId());
        assertEquals(place, bookedTicket.getPlace());
        assertEquals(category, bookedTicket.getCategory());

        verify(storage).getById(User.class, userId);
        verify(storage).getById(Event.class, eventId);
        verify(storage).save(any(Ticket.class), eq(Ticket.class));
    }

    @Test
    void testBookTicketWithNonExistentUser() {
        long userId = 1L;
        long eventId = 100L;
        int place = 10;
        Ticket.Category category = Ticket.Category.STANDARD;

        when(storage.getById(User.class, userId)).thenReturn(null);
        when(storage.getById(Event.class, eventId)).thenReturn(mock(Event.class));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            ticketService.bookTicket(userId, eventId, place, category);
        });

        assertEquals("User with ID " + userId + " does not exist.", exception.getMessage());
    }

    @Test
    void testBookTicketWithNonExistentEvent() {
        long userId = 1L;
        long eventId = 100L;
        int place = 10;
        Ticket.Category category = Ticket.Category.STANDARD;

        when(storage.getById(User.class, userId)).thenReturn(mock(User.class));
        when(storage.getById(Event.class, eventId)).thenReturn(null);

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            ticketService.bookTicket(userId, eventId, place, category);
        });

        assertEquals("Event with ID " + eventId + " does not exist.", exception.getMessage());
    }
    @Test
    void testGetBookedTicketsByUser() {
        User user = new UserImpl();
        user.setId(1L);
        int pageSize = 2;
        int pageNum = 0;

        List<Ticket> tickets = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Ticket ticket = new TicketImpl();
                    ticket.setUserId(user.getId());
                    return ticket;
                })
                .collect(Collectors.toList());

        when(storage.selectAll(Ticket.class)).thenReturn(tickets);

        List<Ticket> result = ticketService.getBookedTickets(user, pageSize, pageNum);

        assertEquals(pageSize, result.size());
        assertTrue(result.stream().allMatch(t -> t.getUserId() == user.getId()));

        verify(storage).selectAll(Ticket.class);
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event event = new EventImpl();
        event.setId(100L);
        int pageSize = 2;
        int pageNum = 0;

        List<Ticket> tickets = IntStream.range(0, 5)
                .mapToObj(i -> {
                    Ticket ticket = new TicketImpl();
                    ticket.setEventId(event.getId());
                    return ticket;
                })
                .collect(Collectors.toList());

        when(storage.selectAll(Ticket.class)).thenReturn(tickets);

        List<Ticket> result = ticketService.getBookedTickets(event, pageSize, pageNum);

        assertEquals(pageSize, result.size());
        assertTrue(result.stream().allMatch(t -> t.getEventId() == event.getId()));

        verify(storage).selectAll(Ticket.class);
    }

    @Test
    void testCancelTicket() {
        long ticketId = 1L;
        when(storage.delete(Ticket.class, ticketId)).thenReturn(true);

        boolean result = ticketService.cancelTicket(ticketId);

        assertTrue(result);
        verify(storage).delete(Ticket.class, ticketId);
    }
}