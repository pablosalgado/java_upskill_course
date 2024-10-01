package com.epam.pablo.task01.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.pablo.task01.exception.UserNotFoundException;
import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;

    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(@RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<User> userPage = bookingFacade.getAllUsers(size, page);
        return new ResponseEntity<>(userPage.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return Optional.ofNullable(bookingFacade.getUserById(id))
                       .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return Optional.ofNullable(user)
                .map(this::safelyCreateUser)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<User> safelyCreateUser(User user) {
        try {
            User createdUser = bookingFacade.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return Optional.ofNullable(user)
                .map(u -> safelyUpdateUser(id, u))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    private ResponseEntity<User> safelyUpdateUser(Long id, User u) {
        try {
            User updatedUser = bookingFacade.updateUser(id, u);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        bookingFacade.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/refill")
    public ResponseEntity<Void> refillAccount(@PathVariable Long id, @RequestParam String amount) {
        bookingFacade.refillAccount(id, BigDecimal.valueOf(Double.parseDouble(amount)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}