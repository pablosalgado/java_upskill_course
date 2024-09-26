package com.epam.pablo.task01.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public void testListUsers() throws Exception {
        Page<User> userPage = new PageImpl<>(Arrays.asList(new User(), new User()));
        when(bookingFacade.getAllUsers(any(Integer.class), any(Integer.class))).thenReturn(userPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));

        verify(bookingFacade).getAllUsers(any(Integer.class), any(Integer.class));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        when(bookingFacade.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/show"))
                .andExpect(model().attributeExists("user"));

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void testNewUser() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testCreateUser() throws Exception {
        when(bookingFacade.createUser(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/users")
                .param("name", "John Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(bookingFacade).createUser(any(User.class));
    }

    @Test
    public void testEditUser() throws Exception {
        User user = new User();
        when(bookingFacade.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/edit"))
                .andExpect(model().attributeExists("user"));

        verify(bookingFacade).getUserById(1L);
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(bookingFacade.updateUser(eq(1L), any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/users/1")
                .param("name", "John Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(bookingFacade).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(bookingFacade.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(get("/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(bookingFacade).deleteUser(1L);
    }

    @Test
    public void testRefillAccount() throws Exception {
        doNothing().when(bookingFacade).refillAccount(eq(1L), any(BigDecimal.class));

        mockMvc.perform(post("/users/1/refill")
                .param("amount", "100.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/1"));

        verify(bookingFacade).refillAccount(eq(1L), any(BigDecimal.class));
    }
}