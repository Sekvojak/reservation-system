package com.dominik.reservation.controller;


import com.dominik.reservation.dto.UserCreateRequest;
import com.dominik.reservation.entity.User;
import com.dominik.reservation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(
                request.username(),
                request.email()
        );
    }

}
