package com.epam.pablo.task01.integration.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncTicketControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingFacade bookingFacade;

    private Long userId;

    private Long eventId;

    private Long ticketId;

    @BeforeAll
    public void init() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);

        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        var user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        var u = bookingFacade.createUser(user);
        userId = u.getId();

        bookingFacade.refillAccount(userId, BigDecimal.TEN);

        var event = new Event();
        event.setTitle("Test Event");
        event.setTicketPrice(BigDecimal.TEN);
        event.setDate(LocalDateTime.now());

        var e = bookingFacade.createEvent(event).getId();
        eventId = e;

        var t = bookingFacade.bookTicket(userId, eventId, 1, Ticket.Category.PREMIUM);
        ticketId = t.getId();
    }

    @Test
    public void testListAllTickets() throws Exception {
        mockMvc.perform(get("/tickets")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testListTicketsByUser() throws Exception {
        mockMvc.perform(get("/tickets/user/" + userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateTicket() throws Exception {
        String ticketJson = """
                {
                    "eventId": 1,
                    "categoryId": 2,
                    "seat": 100
                }
                """;

        mockMvc.perform(post("/tickets/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("createTicketQueue", new Object[]{ticketJson, userId});
    }

    @Test
    public void testDeleteTicket() throws Exception {
        mockMvc.perform(delete("/tickets/" + ticketId))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("deleteTicketQueue", ticketId);
    }

}