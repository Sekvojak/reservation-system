package com.dominik.reservation.controller;


import com.dominik.reservation.dto.UserCreateRequest;
import com.dominik.reservation.dto.UserResponse;
import com.dominik.reservation.entity.User;
import com.dominik.reservation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(
                request.username(),
                request.email()
        );
    }

    @GetMapping
    public Page<UserResponse> list(Pageable pageable) {
        return userService.list(pageable);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
