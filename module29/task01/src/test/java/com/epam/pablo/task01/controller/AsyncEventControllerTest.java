package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;

@WebMvcTest(AsyncEventController.class)
public class AsyncEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingFacade bookingFacade;

    @MockBean
    private JmsTemplate jmsTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListEvents() throws Exception {
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
    public void testGetEvent() throws Exception {
        Event event = new Event();
        when(bookingFacade.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void testGetEventNotFound() throws Exception {
        when(bookingFacade.getEventById(1L)).thenReturn(null);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void testCreateEvent() throws Exception {
        String eventJson = "{\"title\":\"Sample Event\"}";

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event creation request received"));

        verify(jmsTemplate).convertAndSend(eq("createEventQueue"), eq(eventJson));
    }

    @Test
    public void testUpdateEvent() throws Exception {
        String eventJson = "{\"title\":\"Updated Event\"}";

        mockMvc.perform(put("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event update request received"));

        verify(jmsTemplate).convertAndSend(eq("updateEventQueue"), any(Object[].class));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Event deletion request received"));

        verify(jmsTemplate).convertAndSend(eq("deleteEventQueue"), eq(1L));
    }
}