package com.dominik.reservation.service;

import com.dominik.reservation.dto.UserResponse;
import com.dominik.reservation.entity.User;
import com.dominik.reservation.exception.ConflictException;
import com.dominik.reservation.exception.NotFoundException;
import com.dominik.reservation.repository.ReservationRepository;
import com.dominik.reservation.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;


    public UserService(UserRepository userRepository, ReservationRepository reservationRepository) {

        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public UserResponse createUser(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public Page<UserResponse> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        return toResponse(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found " + id);
        }

        if (reservationRepository.countByUserIdAndCanceledFalse(id) > 0) {
            throw new ConflictException("User has reservations, cannot delete");
        }

        userRepository.deleteById(id);
    }
}
