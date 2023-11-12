package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@RestController
@RequestMapping("/userAPI")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getUser(Authentication authentication) {
        User user = new User();
        if (authentication != null) {
            String email = authentication.getName();
            user = userService.findUserByEmail(email);
        } else {
            user.setEmail("not authorized!");
        }
        return ResponseEntity.ok(user);
    }
}
