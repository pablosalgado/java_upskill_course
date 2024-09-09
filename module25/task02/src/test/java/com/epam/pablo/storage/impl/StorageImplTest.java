package com.epam.pablo.storage.impl;

import com.epam.pablo.entity.Event;
import com.epam.pablo.entity.Ticket;
import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.EventImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class StorageImplTest {

    @InjectMocks
    private StorageImpl storage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectAll_EmptyStorage() {
        assertTrue(storage.selectAll(Event.class).isEmpty());
        assertTrue(storage.selectAll(Ticket.class).isEmpty());
        assertTrue(storage.selectAll(User.class).isEmpty());
    }

    @Test
    void testSelectAll_WithObjects() {
        var obj1 = mock(Event.class);
        var obj2 = mock(Ticket.class);
        var obj3 = mock(Ticket.class);

        storage.save(obj1, Event.class);
        storage.save(obj2, Ticket.class);
        storage.save(obj3, Ticket.class);

        List<Event> results = storage.selectAll(Event.class);
        assertEquals(1, results.size());
        assertTrue(results.contains(obj1));

        results = storage.selectAll(Ticket.class);
        assertEquals(2, results.size());
        assertTrue(results.contains(obj2));
        assertTrue(results.contains(obj3));

        results = storage.selectAll(User.class);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSave() {
        var obj1 = mock(Event.class);
        var obj2 = mock(Event.class);

        storage.save(obj1, Event.class);
        storage.save(obj2, Event.class);

        verify(obj1).setId(1L);
        verify(obj2).setId(2L);
    }

    @Test
    void testUpdate() {
        var obj1 = new EventImpl();
        obj1.setTitle("Event 1");
        storage.save(obj1, Event.class);

        var obj2 = new EventImpl();
        obj2.setId(1L);
        obj2.setTitle("Event 2");

        storage.update(obj2, Event.class);
        var result = storage.getById(Event.class, 1L);

        assertEquals(obj2, result);
    }

    @Test
    void testDelete() {
        var obj1 = new EventImpl();
        obj1.setTitle("Event 1");
        storage.save(obj1, Event.class);

        assertTrue(storage.delete(Event.class, 1L));
        assertTrue(storage.selectAll(Event.class).isEmpty());
    }
}
