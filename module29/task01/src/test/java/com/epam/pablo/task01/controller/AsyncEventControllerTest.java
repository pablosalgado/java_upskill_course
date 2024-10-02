package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AsyncEventControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private AsyncEventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void listEventsReturnsEventsPage() throws Exception {
        List<Event> events = Arrays.asList(new Event(), new Event());
        Page<Event> page = new PageImpl<>(events);
        when(bookingFacade.getAllEvents(10, 0)).thenReturn(page);

        mockMvc.perform(get("/events")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(bookingFacade).getAllEvents(10, 0);
    }

    @Test
    public void getEventReturnsEvent() throws Exception {
        Event event = new Event();
        when(bookingFacade.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void getEventReturnsNotFoundWhenEventDoesNotExist() throws Exception {
        when(bookingFacade.getEventById(1L)).thenReturn(null);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void createEventReturnsAccepted() throws Exception {
        String eventJson = "{\"title\":\"Sample Event\"}";

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event creation request received"));

        verify(jmsTemplate).convertAndSend(eq("createEventQueue"), eq(eventJson));
    }

    @Test
    public void updateEventReturnsAccepted() throws Exception {
        String eventJson = "{\"title\":\"Updated Event\"}";

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event update request received"));

        verify(jmsTemplate).convertAndSend(eq("updateEventQueue"), any(Object[].class));
    }

    @Test
    public void deleteEventReturnsAccepted() throws Exception {
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event deletion request received"));

        verify(jmsTemplate).convertAndSend(eq("deleteEventQueue"), eq(1L));
    }

}