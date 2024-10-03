package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Profile("sync")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
        return ResponseEntity.ok(userPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return bookingFacade.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = bookingFacade.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            logger.error("User creation failed due to: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = bookingFacade.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("User update failed due to: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            bookingFacade.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("User deletion failed due to: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/refill")
    public ResponseEntity<Void> refillAccount(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            bookingFacade.refillAccount(id, amount);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Account refill failed due to: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}