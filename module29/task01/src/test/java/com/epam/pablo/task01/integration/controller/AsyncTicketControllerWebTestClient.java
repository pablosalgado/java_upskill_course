package com.epam.pablo.task01.integration.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncTicketControllerWebTestClient {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingFacade bookingFacade;

    private Long userId;

    private Long eventId;

    private Long ticketId;

    @BeforeAll
    public void init() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);

        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        var user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        var u = bookingFacade.createUser(user);
        userId = u.getId();

        bookingFacade.refillAccount(userId, BigDecimal.TEN);

        var event = new Event();
        event.setTitle("Test Event");
        event.setTicketPrice(BigDecimal.TEN);
        event.setDate(LocalDateTime.now());

        var e = bookingFacade.createEvent(event).getId();
        eventId = e;

        var t = bookingFacade.bookTicket(userId, eventId, 1, Ticket.Category.PREMIUM);
        ticketId = t.getId();
    }

    @Test
    public void testListAllTickets() {
        webTestClient.get().uri("/tickets?page=0&size=10")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testListTicketsByUser() {
        webTestClient.get().uri("/tickets/user/" + userId + "?page=0&size=10")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testCreateTicket() {
        String ticketJson = """
                {
                    "eventId": 1,
                    "categoryId": 2,
                    "seat": 100
                }
                """;
        webTestClient.post().uri("/tickets/user/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ticketJson)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("Ticket creation request accepted");

        verify(jmsTemplate).convertAndSend("createTicketQueue", new Object[]{ticketJson, userId});
    }

    @Test
    public void testDeleteTicket() {
        webTestClient.delete().uri("/tickets/" + ticketId)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("Event deletion request received");
    }

}