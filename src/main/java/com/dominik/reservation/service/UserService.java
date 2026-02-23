package com.dominik.reservation.service;

import com.dominik.reservation.entity.User;
import com.dominik.reservation.exception.ConflictException;
import com.dominik.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
