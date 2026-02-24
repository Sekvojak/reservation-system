package com.dominik.reservation.service;

import com.dominik.reservation.exception.ConflictException;
import com.dominik.reservation.exception.NotFoundException;
import com.dominik.reservation.repository.ReservationRepository;
import com.dominik.reservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        userService = new UserService(userRepository, reservationRepository);
    }

    @Test
    void delete_shouldThrowNotFound_whenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.delete((1L)));

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void delete_shouldThrowConflict_whenUserHasActiveReservations() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.countByUserIdAndCanceledFalse(1L)).thenReturn(1L);

        assertThrows(ConflictException.class, () -> userService.delete(1L));

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void delete_shouldDeleteUser_whenNoReservations() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.countByUserIdAndCanceledFalse(1L)).thenReturn(0L);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_shouldIncludeUserIdInNotFoundMessage() {
        when(userRepository.existsById(33L)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> userService.delete(33L));

        assertTrue(ex.getMessage().contains("33"));
        verify(userRepository, never()).deleteById(anyLong());
    }

}
