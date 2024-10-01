package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.pablo.task01.exception.EventNotFoundException;
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
    public void listEventsReturnsEmptyListWhenNoEventsExist() throws Exception {
        Page<Event> eventPage = new PageImpl<>(Arrays.asList());
        when(bookingFacade.getAllEvents(any(Integer.class), any(Integer.class))).thenReturn(eventPage);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookingFacade).getAllEvents(any(Integer.class), any(Integer.class));
    }

    @Test
    public void listEventsReturnsEventsWhenEventsExist() throws Exception {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Conference");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Seminar");

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(event1, event2));
        when(bookingFacade.getAllEvents(any(Integer.class), any(Integer.class))).thenReturn(eventPage);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Conference"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Seminar"));

        verify(bookingFacade).getAllEvents(any(Integer.class), any(Integer.class));
    }

    @Test
    public void getEventReturnsNotFoundWhenEventDoesNotExist() throws Exception {
        when(bookingFacade.getEventById(1L)).thenReturn(null);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getEventById(1L);
    }

    @Test
    public void createEventReturnsCreatedWhenEventIsValid() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Conference");
        event.setTicketPrice(BigDecimal.TEN);

        when(bookingFacade.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Conference",
                                    "ticketPrice": "10"
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Conference"))
                .andExpect(jsonPath("$.ticketPrice").value("10"));

        verify(bookingFacade).createEvent(any(Event.class));
    }

    @Test
    public void createEventWhenEmailAlreadyExists() throws Exception {
        when(bookingFacade.createEvent(any(Event.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Conference",
                                    "ticketPrice": "10"
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEventReturnsBadRequestWhenEventIsInvalid() throws Exception {
        when(bookingFacade.createEvent(any(Event.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEventReturnsBadRequestWhenEventIsInvalid() throws Exception {
        when(bookingFacade.updateEvent(eq(1L), any(Event.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateEventReturnsUpdatedEventWhenEventIsValid() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Conference");

        when(bookingFacade.updateEvent(eq(1L), any(Event.class))).thenReturn(event);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Conference"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Conference"));

        verify(bookingFacade).updateEvent(eq(1L), any(Event.class));
    }

    @Test
    public void updateEventReturnsNotFoundWhenEventDoesNotExist() throws Exception {
        when(bookingFacade.updateEvent(eq(1L), any(Event.class))).thenThrow(EventNotFoundException.class);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Conference"
                                }"""))
                .andExpect(status().isNotFound());

        verify(bookingFacade).updateEvent(eq(1L), any(Event.class));
    }

    @Test
    public void deleteEventWhenEventDoesNotExist() throws Exception {
        when(bookingFacade.deleteEvent(1L)).thenReturn(false);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        verify(bookingFacade).deleteEvent(1L);
    }

    @Test
    public void deleteEventWhenEventExists() throws Exception {
        when(bookingFacade.deleteEvent(1L)).thenReturn(true);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        verify(bookingFacade).deleteEvent(1L);
    }
}