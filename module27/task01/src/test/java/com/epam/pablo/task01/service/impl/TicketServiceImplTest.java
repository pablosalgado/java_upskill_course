package com.epam.pablo.task01.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.epam.pablo.exception.EventNotFoundException;
import com.epam.pablo.exception.InsufficientFundsException;
import com.epam.pablo.exception.UserNotFoundException;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.model.factory.TicketFactory;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;

public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketFactory ticketFactory;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookTicketWhenUserHasEnoughMoney() {
        var ticket = createTicket();

        var user = createUser();
        user.addFundsToAccount(BigDecimal.valueOf(100));

        var event = createEvent();
        event.setTicketPrice(BigDecimal.valueOf(50));

        when(
            userRepository.findById(1L)
        ).thenReturn(
            Optional.of(user)
        );

        when(
            eventRepository.findById(2L)
        ).thenReturn(
            Optional.of(event)
        );

        when(
            ticketFactory.createTicket(
                user,
                event,
                10,
                Ticket.Category.PREMIUM
            )
        ).thenReturn(ticket);

        when(
            ticketRepository.save(
                any(Ticket.class)
            )
        ).thenReturn(ticket);

        Ticket result = ticketService.bookTicket(1L, 2L, 10, Ticket.Category.PREMIUM);

        verify(ticketRepository).save(ticket);
        verify(userRepository).findById(1L);
        verify(eventRepository).findById(2L);
        verify(ticketFactory).createTicket(user, event, 10, Ticket.Category.PREMIUM);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), user.getAccountBalance());
    }

    @Test
    public void testBookTicketWhenUserHasNotEnoughMoney() {
        var ticket = createTicket();

        var user = createUser();
        user.addFundsToAccount(BigDecimal.valueOf(10));

        var event = createEvent();
        event.setTicketPrice(BigDecimal.valueOf(50));

        when(
            userRepository.findById(1L)
        ).thenReturn(
            Optional.of(user)
        );

        when(
            eventRepository.findById(2L)
        ).thenReturn(
            Optional.of(event)
        );

        when(
            ticketFactory.createTicket(
                user,
                event,
                10,
                Ticket.Category.PREMIUM
            )
        ).thenReturn(ticket);

        assertThrows(InsufficientFundsException.class, () -> {
            ticketService.bookTicket(1L, 2L, 10, Ticket.Category.PREMIUM);
        });

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testBookTicketEventNotFound() {
        long userId = 1L;
        long eventId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> {
            ticketService.bookTicket(userId, eventId, 10, Ticket.Category.PREMIUM);
        });

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testBookTicketUserNotFound() {
        long userId = 1L;
        long eventId = 2L;

        when(userRepository.existsById(userId)).thenReturn(false);
        when(eventRepository.existsById(eventId)).thenReturn(true);

        assertThrows(UserNotFoundException.class, () -> {
            ticketService.bookTicket(userId, eventId, 10, Ticket.Category.PREMIUM);
        });

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testGetBookedTicketsByUser() {
        User user = new User();
        user.setId(1L);
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        Page<Ticket> page = new PageImpl<>(tickets);

        when(ticketRepository.findByUserId(eq(1L), any())).thenReturn(page);
        List<Ticket> result = ticketService.getBookedTickets(user, 10, 1);

        verify(ticketRepository).findByUserId(eq(1L), any(PageRequest.class));
        assertEquals(tickets, result);
    }

    @Test
    public void testGetBookedTicketsByEvent() {
        Event event = new Event();
        event.setId(2L);
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        Page<Ticket> page = new PageImpl<>(tickets);

        when(ticketRepository.findByEventId(eq(2L), any())).thenReturn(page);
        List<Ticket> result = ticketService.getBookedTickets(event, 10, 1);

        verify(ticketRepository).findByEventId(eq(2L), any(PageRequest.class));
        assertEquals(tickets, result);
    }

    @Test
    public void testCancelTicket() {
        var ticket = mock(Ticket.class);
        var user = mock(User.class);
        var event = mock(Event.class);

        when(ticket.getUser()).thenReturn(user);
        when(ticket.getEvent()).thenReturn(event);
        when(ticket.getPlace()).thenReturn(10);
        when(ticket.getCategory()).thenReturn(Ticket.Category.PREMIUM);

        when(user.getAccountBalance()).thenReturn(BigDecimal.valueOf(50));
        when(event.getTicketPrice()).thenReturn(BigDecimal.valueOf(50));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(user).addFundsToAccount(any(BigDecimal.class));
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(ticketRepository).deleteById(1L);

        boolean result = ticketService.cancelTicket(1L);

        verify(user).addFundsToAccount(BigDecimal.valueOf(50));
        verify(userRepository).save(user);
        verify(ticketRepository).deleteById(1L);
        assertTrue(result);
    }

    @Test
    public void testCancelTicketNotFound() {
        when(ticketRepository.existsById(1L)).thenReturn(false);
        boolean result = ticketService.cancelTicket(1L);

        assertFalse(result);
    }

    private Ticket createTicket() {
        var ticket = new Ticket();
        ticket.setPlace(10);
        ticket.setCategory(Ticket.Category.PREMIUM);
        return ticket;
    }

    private User createUser() {
        var user = new User();
        user.setId(1L);
        return user;
    }

    private Event createEvent() {
        var event = new Event();
        event.setId(2L);
        return event;
    }

}