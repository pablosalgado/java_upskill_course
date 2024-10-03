package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AsyncUserControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private AsyncUserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void listUsersReturnsUsersPage() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        Page<User> page = new PageImpl<>(users);
        when(bookingFacade.getAllUsers(10, 0)).thenReturn(page);

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(bookingFacade).getAllUsers(10, 0);
    }

    @Test
    public void getUserReturnsUser() throws Exception {
        User user = new User();
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void getUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void createUserReturnsAccepted() throws Exception {
        String userJson = "{\"name\":\"Sample User\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User creation request received"));

        verify(jmsTemplate).convertAndSend(eq("createUserQueue"), eq(userJson));
    }

    @Test
    public void updateUserReturnsAccepted() throws Exception {
        String userJson = "{\"name\":\"Updated User\"}";

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User update request received"));

        verify(jmsTemplate).convertAndSend(eq("updateUserQueue"), any(Object[].class));
    }

    @Test
    public void deleteUserReturnsAccepted() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User deletion request received"));

        verify(jmsTemplate).convertAndSend(eq("deleteUserQueue"), eq(1L));
    }

    @Test
    public void refillAccountReturnsOk() throws Exception {
        mockMvc.perform(post("/users/1/refill")
                        .param("amount", "100.00"))
                .andExpect(status().isOk());

        verify(bookingFacade).refillAccount(1L, new BigDecimal("100.00"));
    }

    @Test
    public void refillAccountReturnsNotFoundWhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Error")).when(bookingFacade).refillAccount(1L, new BigDecimal("100.00"));

        mockMvc.perform(post("/users/1/refill")
                        .param("amount", "100.00"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).refillAccount(1L, new BigDecimal("100.00"));
    }
    
}