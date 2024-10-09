package com.epam.pablo.task01.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.epam.pablo.task01.model.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.springframework.test.context.TestContextManager;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "async"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsyncUserControllerTestRestTemplate {

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

        ResponseEntity<String> response = restTemplate.postForEntity("/users", userJson, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("User creation request received");
        verify(jmsTemplate).convertAndSend("createUserQueue", userJson);
    }

    @Test
    public void getUserReturnsUser() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/" + userId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"name\":\"John Doe\"");
        assertThat(response.getBody()).contains("\"email\":\"john.doe@example.com\"");
    }

    @Test
    public void getUserReturnsNotFoundWhenUserDoesNotExist() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUserReturnsAccepted() {
        String userJson = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(userJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/users/" + userId, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("User update request received");

        verify(jmsTemplate).convertAndSend("updateUserQueue", new Object[]{userId, userJson});
    }

    @Test
    public void deleteUserReturnsAccepted() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/users/" + userId, HttpMethod.DELETE, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isEqualTo("User deletion request received");

        verify(jmsTemplate).convertAndSend("deleteUserQueue", userId);
    }

}