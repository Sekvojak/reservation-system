package com.dominik.reservation.controller;

import com.dominik.reservation.dto.ReservationCreateRequest;
import com.dominik.reservation.dto.ReservationResponse;
import com.dominik.reservation.dto.ReservationUpdateRequest;
import com.dominik.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody ReservationCreateRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping
    public Page<ReservationResponse> list(
            @RequestParam(required = false) Long facilityId,
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        return reservationService.list(facilityId, userId, pageable);
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ReservationResponse update(@PathVariable Long id,
                                      @RequestBody ReservationUpdateRequest request) {
        return reservationService.update(id, request);

    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }

}
