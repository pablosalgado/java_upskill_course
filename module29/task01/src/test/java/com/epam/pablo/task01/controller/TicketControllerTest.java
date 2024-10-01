package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void listAllTicketsReturnsTicketsPage() throws Exception {
        Page<Ticket> ticketPage = new PageImpl<>(Arrays.asList(new Ticket(), new Ticket()));
        when(bookingFacade.getBookedTickets(any(Integer.class), any(Integer.class))).thenReturn(ticketPage);

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(bookingFacade).getBookedTickets(any(Integer.class), any(Integer.class));
    }

    @Test
    public void listTicketsByUserReturnsTicketsPageWhenUserExists() throws Exception {
        User user = new User();
        Page<Ticket> ticketPage = new PageImpl<>(Arrays.asList(new Ticket(), new Ticket()));
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.of(user));
        when(bookingFacade.getBookedTicketsByUser(any(User.class), any(Integer.class), any(Integer.class))).thenReturn(ticketPage);

        mockMvc.perform(get("/tickets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(bookingFacade).getUserById(1L);
        verify(bookingFacade).getBookedTicketsByUser(any(User.class), any(Integer.class), any(Integer.class));
    }

    @Test
    public void listTicketsByUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/user/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void newTicketReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/new/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getUserById(1L);
    }
}