package com.epam.pablo.task01.integration.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncEventControllerTestRestTemplate {

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private Long eventId;

    @BeforeAll
    void init() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);

        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        Event event = new Event();
        event.setTitle("Concert");
        event.setDate(LocalDateTime.parse("2021-12-01T19:30:00"));
        eventId = bookingFacade.createEvent(event).getId();
    }

    @Test
    public void testCreateEvent() {
        String eventJson = """
                {
                    "title": "Concert",
                    "date": "2021-12-01T19:30:00"
                }
                """;

        ResponseEntity<String> response = restTemplate.postForEntity("/events", eventJson, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("Event creation request received");
        verify(jmsTemplate).convertAndSend("createEventQueue", eventJson);
    }

    @Test
    public void getEventReturnsEvent() {
        ResponseEntity<String> response = restTemplate.getForEntity("/events/" + eventId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"title\":\"Concert\"");
        assertThat(response.getBody()).contains("\"date\":\"2021-12-01T19:30:00\"");
    }

    @Test
    public void getEventReturnsNotFoundWhenEventDoesNotExist() {
        ResponseEntity<String> response = restTemplate.getForEntity("/events/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateEventReturnsAccepted() {
        String eventJson = """
                {
                    "title": "Updated Concert",
                    "date": "2021-12-02T20:00:00"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(eventJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/events/" + eventId, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("Event update request received");

        verify(jmsTemplate).convertAndSend("updateEventQueue", new Object[]{eventId, eventJson});
    }

    @Test
    public void deleteEventReturnsAccepted() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/events/" + eventId, HttpMethod.DELETE, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("Event deletion request received");

        verify(jmsTemplate).convertAndSend("deleteEventQueue", eventId);
    }

}