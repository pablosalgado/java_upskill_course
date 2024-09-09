package com.epam.pablo.service.impl;

import com.epam.pablo.entity.Event;
import com.epam.pablo.service.EventService;
import com.epam.pablo.storage.Storage;
import com.epam.pablo.storage.impl.StorageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventServiceImplTest {
    @Mock
    private Storage storage;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEventById() {
        var event = mock(Event.class);

        when(event.getId()).thenReturn(1L);
        when(storage.getById(Event.class, 1L)).thenReturn(event);

        var result = eventService.getEventById(1L);

        assertEquals(event, result);
    }

    @Test
    void createEvent() {
        Event event = mock(Event.class);
        when(storage.save(event, Event.class)).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertEquals(event, createdEvent);
        verify(storage).save(event, Event.class);
    }

    @Test
    void updateEvent() {
        Event event = mock(Event.class);
        when(storage.update(event, Event.class)).thenReturn(event);

        Event updatedEvent = eventService.updateEvent(event);

        assertEquals(event, updatedEvent);
        verify(storage).update(event, Event.class);
    }

    @Test
    void deleteEvent() {
        long eventId = 1L;
        when(storage.delete(Event.class, eventId)).thenReturn(true);

        boolean result = eventService.deleteEvent(eventId);

        assertTrue(result);
        verify(storage).delete(Event.class, eventId);
    }

    @Test
    void testGetEventsByTitle() {
        String title = "Event Title";
        var allEvents = IntStream.range(0, 10).mapToObj(i -> {
            Event event = mock(Event.class);
            when(event.getTitle()).thenReturn(i % 2 == 0 ? title : "Other Title");
            return event;
        }).toList();

        when(storage.selectAll(Event.class)).thenReturn(allEvents);

        int pageSize = 3;
        int pageNum = 0;

        var events = eventService.getEventsByTitle(title, pageSize, pageNum);

        assertEquals(pageSize, events.size());
        assertTrue(events.stream().allMatch(e -> e.getTitle().equals(title)));

        verify(storage).selectAll(Event.class);
    }

    @Test
    void testGetEventsForDay() {
        var day = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        var allEvents = IntStream.range(0, 10).mapToObj(i -> {
            Event event = mock(Event.class);
            when(event.getDate()).thenReturn(i % 2 == 0 ? day : new Date(day.getTime() + 86400000L * i));
            return event;
        }).toList();

        when(storage.selectAll(Event.class)).thenReturn(allEvents);

        int pageSize = 3;
        int pageNum = 0;

        var events = eventService.getEventsForDay(day, pageSize, pageNum);

        assertEquals(pageSize, events.size()); // Check if the page size is correct
        assertTrue(events.stream().allMatch(e -> e.getDate().equals(day))); // Check if all events have the correct day

        verify(storage).selectAll(Event.class);
    }
}