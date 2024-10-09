package com.epam.pablo.task01.integration.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "async"})
public class AsyncUserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testCreateUser() throws Exception {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), ArgumentMatchers.<Object>any());

        String jsonUser = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("createUserQueue", jsonUser);
    }

    @Test
    public void testUpdateUser() throws Exception {
        String jsonUser = """
                {
                    "name": "John Doe",
                    "email": "john.doe@example.com"
                }
                """;

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("updateUserQueue", new Object[]{1L, jsonUser});
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isAccepted());

        verify(jmsTemplate).convertAndSend("deleteUserQueue", 1L);
    }

    @Test
    public void testRefillAccount() throws Exception {
        var user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        bookingFacade.createUser(user);

        mockMvc.perform(post("/users/1/refill")
                        .param("amount", "100.00"))
                .andExpect(status().isOk());
    }
}