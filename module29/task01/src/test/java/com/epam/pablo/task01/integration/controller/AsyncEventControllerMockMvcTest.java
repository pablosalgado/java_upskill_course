package com.epam.pablo.task01.integration.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.repository.EventRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "async"})
public class AsyncEventControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setUp() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateEvent() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), ArgumentMatchers.<Object>any());

        String jsonEvent = """
                {
                    "title": "Concert",
                    "date": "2021-12-01T19:30:00"
                }
                """;

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvent))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("createEventQueue", jsonEvent);
    }

    @Test
    public void testUpdateEvent() throws Exception {
        String jsonEvent = """
                {
                    "title": "Updated Concert",
                    "date": "2021-12-02T20:00:00"
                }
                """;

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvent))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("updateEventQueue", new Object[]{1L, jsonEvent});
    }

    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("deleteEventQueue", 1L);
    }
}