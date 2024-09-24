package com.epam.pablo.task01.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.User;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;

    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<User> userPage = bookingFacade.getAllUsers(size, page);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "users/list";
    }

    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable Long id) {
        model.addAttribute("user", bookingFacade.getUserById(id));
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user) {
        bookingFacade.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(Model model, @PathVariable Long id) {
        model.addAttribute("user", bookingFacade.getUserById(id));
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        bookingFacade.updateUser(id, user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        bookingFacade.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/{id}/refill")
    public String refillAccount(@PathVariable Long id, @RequestParam String amount) {
        bookingFacade.refillAccount(id, BigDecimal.valueOf(Double.parseDouble(amount)));
        return "redirect:/users/edit/" + id;
    }
}
