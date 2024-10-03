package com.epam.pablo.task01.controller;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
@Profile("async")
public class AsyncUserController {

    private static final Logger logger = LoggerFactory.getLogger(AsyncUserController.class);
    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    private final JmsTemplate jmsTemplate;

    public AsyncUserController(JmsTemplate jmsTemplate, BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
        this.jmsTemplate = jmsTemplate;
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
    public ResponseEntity<String> createUser(@RequestBody String user) {
        jmsTemplate.convertAndSend("createUserQueue", user);
        return ResponseEntity.accepted().body("User creation request received");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody String user) {
        jmsTemplate.convertAndSend("updateUserQueue", new Object[]{id, user});
        return ResponseEntity.accepted().body("User update request received");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        jmsTemplate.convertAndSend("deleteUserQueue", id);
        return ResponseEntity.accepted().body("User deletion request received");
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