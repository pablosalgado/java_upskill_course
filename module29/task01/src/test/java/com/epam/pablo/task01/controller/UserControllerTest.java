package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Arrays;

import com.epam.pablo.task01.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;

public class UserControllerTest {

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void listUsersReturnsEmptyListWhenNoUsersExist() throws Exception {
        Page<User> userPage = new PageImpl<>(Arrays.asList());
        when(bookingFacade.getAllUsers(any(Integer.class), any(Integer.class))).thenReturn(userPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookingFacade).getAllUsers(any(Integer.class), any(Integer.class));
    }

    @Test
    public void listUsersReturnsUsersWhenUsersExist() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");

        Page<User> userPage = new PageImpl<>(Arrays.asList(user1, user2));
        when(bookingFacade.getAllUsers(any(Integer.class), any(Integer.class))).thenReturn(userPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(bookingFacade).getAllUsers(any(Integer.class), any(Integer.class));
    }

    @Test
    public void getUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void createUserReturnsCreatedWhenUserIsValid() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(bookingFacade.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "John Doe",
                                    "email": "john.doe@example.com"
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(bookingFacade).createUser(any(User.class));
    }

    @Test
    public void createUserWhenEmailAlreadyExists() throws Exception {
        when(bookingFacade.createUser(any(User.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "John Doe",
                                    "email": "john.doe@example.com"
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserReturnsBadRequestWhenUserIsInvalid() throws Exception {
        when(bookingFacade.createUser(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserReturnsBadRequestWhenUserIsInvalid() throws Exception {
        when(bookingFacade.updateUser(eq(1L), any(User.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserReturnsUpdatedUserWhenUserIsValid() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        when(bookingFacade.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(bookingFacade).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void updateUserReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.updateUser(eq(1L), any(User.class))).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"}"))
                .andExpect(status().isNotFound());

        verify(bookingFacade).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void deleteUserWhenUserDoesNotExist() throws Exception {
        when(bookingFacade.deleteUser(1L)).thenReturn(false);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(bookingFacade).deleteUser(1L);
    }

    @Test
    public void deleteUserWhenUserExists() throws Exception {
        when(bookingFacade.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(bookingFacade).deleteUser(1L);
    }

    @Test
    public void refillAccount_returnsBadRequest_whenAmountIsInvalid() throws Exception {
        mockMvc.perform(post("/users/1/refill")
                        .param("amount", "100"))
                .andExpect(status().isOk());
    }
}