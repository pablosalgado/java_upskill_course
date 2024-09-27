package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;

class TicketControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void testListAllTickets() throws Exception {
        Page<Ticket> ticketPage = new PageImpl<>(Collections.emptyList());
        when(bookingFacade.getBookedTickets(anyInt(), anyInt())).thenReturn(ticketPage);

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/index"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void testListTicketsByUser() throws Exception {
        User user = new User();
        user.setId(1L);
        Page<Ticket> ticketPage = new PageImpl<>(Collections.emptyList());
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(bookingFacade.getBookedTicketsByUser(any(User.class), anyInt(), anyInt())).thenReturn(ticketPage);

        mockMvc.perform(get("/tickets/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/list"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void testNewTicket() throws Exception {
        User user = new User();
        user.setId(1L);
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets/new/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets/new"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void testDeleteTicket() throws Exception {
        User user = new User();
        user.setId(1L);
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        when(bookingFacade.getTicketById(anyLong())).thenReturn(ticket);

        mockMvc.perform(get("/tickets/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/user/1"));
    }
}