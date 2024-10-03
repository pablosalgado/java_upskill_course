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
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AsyncTicketControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private AsyncTicketController ticketController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void listAllTicketsReturnsTicketsPage() throws Exception {
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        Page<Ticket> page = new PageImpl<>(tickets);
        when(bookingFacade.getBookedTickets(10, 0)).thenReturn(page);

        mockMvc.perform(get("/tickets")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(bookingFacade).getBookedTickets(10, 0);
    }

    @Test
    public void listTicketsByUserReturnsTicketsPage() throws Exception {
        User user = new User();
        List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
        Page<Ticket> page = new PageImpl<>(tickets);
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.of(user));
        when(bookingFacade.getBookedTicketsByUser(user, 10, 0)).thenReturn(page);

        mockMvc.perform(get("/tickets/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(bookingFacade).getUserById(1L);
        verify(bookingFacade).getBookedTicketsByUser(user, 10, 0);
    }

    @Test
    public void listTicketsByUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/user/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void createTicketReturnsAccepted() throws Exception {
        String ticketJson = "{\"event\":{\"id\":1},\"place\":1,\"category\":\"STANDARD\"}";

        mockMvc.perform(post("/tickets/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Ticket creation request accepted"));

        verify(jmsTemplate).convertAndSend(eq("createTicketQueue"), any(Object[].class));
    }
}
