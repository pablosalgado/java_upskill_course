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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void createTicketReturnsCreatedTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(bookingFacade.bookTicket(any(Long.class), any(Long.class), any(Integer.class), any(Ticket.Category.class))).thenReturn(ticket);

        mockMvc.perform(post("/tickets/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "event": {
                                        "id": 1
                                    },
                                    "place": 1,
                                    "category": "STANDARD"
                                }
                                """)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingFacade).bookTicket(any(Long.class), any(Long.class), any(Integer.class), any(Ticket.Category.class));
    }

    @Test
    public void deleteTicketReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNoContent());

        verify(bookingFacade).cancelTicket(1L);
    }

    @Test
    public void uploadDefaultTicketsReturnsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "tickets.csv", "text/csv", "some data".getBytes());

        mockMvc.perform(multipart("/tickets/upload").file(file))
                .andExpect(status().isOk());

        verify(bookingFacade).preloadTickets(any(MultipartFile.class));
    }

    @Test
    public void seedTicketsReturnsOk() throws Exception {
        mockMvc.perform(post("/tickets/seed"))
                .andExpect(status().isOk());

        verify(bookingFacade).preloadTickets();
    }

    @Test
    public void downloadAllTicketsReturnsPdf() throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream("PDF content".getBytes());
        when(bookingFacade.generateTicketsPdf()).thenReturn(bis);

        mockMvc.perform(get("/tickets/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=tickets.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));

        verify(bookingFacade).generateTicketsPdf();
    }

    @Test
    public void downloadTicketsByUserReturnsPdf() throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream("PDF content".getBytes());
        when(bookingFacade.generateUserTicketsPdf(1L)).thenReturn(bis);

        mockMvc.perform(get("/tickets/download/user/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=user_tickets.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));

        verify(bookingFacade).generateUserTicketsPdf(1L);
    }
}