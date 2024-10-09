package com.epam.pablo.task01.integration.controller;

import static org.mockito.Mockito.verify;

import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.repository.EventRepository;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncEventControllerWebTestClient {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookingFacade bookingFacade;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;


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
        webTestClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventJson)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("Event creation request received");

        verify(jmsTemplate).convertAndSend("createEventQueue", eventJson);
    }

    @Test
    public void getEventReturnsEvent() {
        webTestClient.get().uri("/events/" + eventId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Concert")
                .jsonPath("$.date").isEqualTo("2021-12-01T19:30:00");
    }

    @Test
    public void getEventReturnsNotFoundWhenEventDoesNotExist() {
        webTestClient.get().uri("/events/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateEventReturnsAccepted() {
        String eventJson = """
                {
                    "title": "Updated Concert",
                    "date": "2021-12-02T20:00:00"
                }
                """;
        webTestClient.put().uri("/events/" + eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventJson)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("Event update request received");

        verify(jmsTemplate).convertAndSend("updateEventQueue", new Object[]{eventId, eventJson});
    }

    @Test
    public void deleteEventReturnsAccepted() {
        webTestClient.delete().uri("/events/" + eventId)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("Event deletion request received");
    }
}