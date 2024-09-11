package com.epam.pablo.task01.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.repository.EventRepository;

public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetEventById() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Event result = eventService.getEventById(1L);
        verify(eventRepository).findById(1L);
        assertEquals(event, result);
    }

    @Test
    public void testGetEventsByTitle() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        Page<Event> page = new PageImpl<>(events);
        when(eventRepository.findByTitleContaining(eq("title"), any())).thenReturn(page);
        List<Event> result = eventService.getEventsByTitle("title", 10, 1);
        verify(eventRepository).findByTitleContaining(eq("title"), any(PageRequest.class));
        assertEquals(events, result);
    }

    @Test
    public void testGetEventsForDay() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        Page<Event> page = new PageImpl<>(events);
        Date date = new Date();
        when(eventRepository.findByDate(eq(date), any())).thenReturn(page);
        List<Event> result = eventService.getEventsForDay(date, 10, 1);
        verify(eventRepository).findByDate(eq(date), any(PageRequest.class));
        assertEquals(events, result);
    }

    @Test
    public void testCreateEvent() {
        Event event = new Event();
        when(eventRepository.save(event)).thenReturn(event);
        Event result = eventService.createEvent(event);
        verify(eventRepository).save(event);
        assertEquals(event, result);
    }

    @Test
    public void testUpdateEvent() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.save(event)).thenReturn(event);
        Event result = eventService.updateEvent(event);
        verify(eventRepository).save(event);
        assertEquals(event, result);
    }

    @Test
    public void testUpdateEventNotFound() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.existsById(1L)).thenReturn(false);
        Event result = eventService.updateEvent(event);
        assertNull(result);
    }

    @Test
    public void testDeleteEvent() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);
        boolean result = eventService.deleteEvent(1L);
        verify(eventRepository).deleteById(1L);
        assertTrue(result);
    }

    @Test
    public void testDeleteEventNotFound() {
        when(eventRepository.existsById(1L)).thenReturn(false);
        boolean result = eventService.deleteEvent(1L);
        assertFalse(result);
    }
}