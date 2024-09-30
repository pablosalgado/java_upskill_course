package com.epam.pablo.task01.service.impl;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.TicketListWrapper;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

public class ImportDataServiceImplTest {

    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private Jaxb2Marshaller marshaller;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Resource resource;

    @InjectMocks
    private ImportDataServiceImpl importDataService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPreloadTickets() throws Exception {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><tickets>...</tickets>";
        ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlData.getBytes());
        when(resourceLoader.getResource("classpath:data.xml")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(xmlInputStream);

        TicketListWrapper ticketListWrapper = new TicketListWrapper();
        List<Ticket> tickets = new ArrayList<>();
        ticketListWrapper.setTickets(tickets);
        when(marshaller.unmarshal(any(StreamSource.class))).thenReturn(ticketListWrapper);

        importDataService.preloadTickets();

        verify(ticketRepository, times(1)).deleteAll();
        verify(eventRepository, times(1)).deleteAll();
        verify(userRepository, times(1)).deleteAll();
        verify(ticketRepository, times(tickets.size())).save(any(Ticket.class));
    }
}