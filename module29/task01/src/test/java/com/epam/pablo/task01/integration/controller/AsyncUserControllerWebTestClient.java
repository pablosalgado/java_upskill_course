package com.epam.pablo.task01.integration.controller;

import static org.mockito.Mockito.verify;

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
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncUserControllerWebTestClient {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookingFacade bookingFacade;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId;

    @BeforeAll
    void init() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);

        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        var user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        userId = bookingFacade.createUser(user).getId();
    }

    @Test
    public void testCreateUser() {
        String userJson = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com"
                }
                """;
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("User creation request received");

        verify(jmsTemplate).convertAndSend("createUserQueue", userJson);
    }

    @Test
    public void getUserReturnsUser() {
        webTestClient.get().uri("/users/" + userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John Doe")
                .jsonPath("$.email").isEqualTo("john.doe@example.com");
    }

    @Test
    public void getUserReturnsNotFoundWhenUserDoesNotExist() {
        webTestClient.get().uri("/users/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateUserReturnsAccepted() {
        String userJson = """
                {
                    "name": "Jane Doe",
                    "email": "jane.doe@example.com"
                }
                """;
        webTestClient.put().uri("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("User update request received");

        verify(jmsTemplate).convertAndSend("updateUserQueue", new Object[]{userId, userJson});
    }

    @Test
    public void deleteUserReturnsAccepted() {
        webTestClient.delete().uri("/users/" + userId)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class).isEqualTo("User deletion request received");
    }
}