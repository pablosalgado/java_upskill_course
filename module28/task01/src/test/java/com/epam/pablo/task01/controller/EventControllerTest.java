package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

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
import com.epam.pablo.task01.model.Event;

public class EventControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testListEvents() throws Exception {
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(new Event(), new Event()));
        when(bookingFacade.getAllEvents(any(Integer.class), any(Integer.class))).thenReturn(eventPage);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));

        verify(bookingFacade).getAllEvents(any(Integer.class), any(Integer.class));
    }

    @Test
    public void testGetEvent() throws Exception {
        Event event = new Event();
        when(bookingFacade.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/show"))
                .andExpect(model().attributeExists("event"));

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void testNewEvent() throws Exception {
        mockMvc.perform(get("/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/new"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    public void testCreateEvent() throws Exception {
        when(bookingFacade.createEvent(any(Event.class))).thenReturn(new Event());

        mockMvc.perform(post("/events")
                .param("name", "Sample Event"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(bookingFacade).createEvent(any(Event.class));
    }

    @Test
    public void testEditEvent() throws Exception {
        Event event = new Event();
        when(bookingFacade.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/events/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/edit"))
                .andExpect(model().attributeExists("event"));

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void testUpdateEvent() throws Exception {
        when(bookingFacade.updateEvent(eq(1L), any(Event.class))).thenReturn(new Event());

        mockMvc.perform(post("/events/1")
                .param("name", "Updated Event"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(bookingFacade).updateEvent(eq(1L), any(Event.class));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        when(bookingFacade.deleteEvent(1L)).thenReturn(true);

        mockMvc.perform(get("/events/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(bookingFacade).deleteEvent(1L);
    }
}