package com.dominik.reservation.service;

import com.dominik.reservation.dto.ReservationCreateRequest;
import com.dominik.reservation.dto.ReservationResponse;
import com.dominik.reservation.dto.ReservationUpdateRequest;
import com.dominik.reservation.entity.Facility;
import com.dominik.reservation.entity.Reservation;
import com.dominik.reservation.entity.User;
import com.dominik.reservation.exception.BadRequestException;
import com.dominik.reservation.exception.ConflictException;
import com.dominik.reservation.exception.NotFoundException;
import com.dominik.reservation.repository.FacilityRepository;
import com.dominik.reservation.repository.ReservationRepository;
import com.dominik.reservation.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository,
                              FacilityRepository facilityRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request) {

        LocalDateTime start = request.startTime();
        LocalDateTime end = request.endTime();

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));

        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new NotFoundException("Facility not found: " + request.facilityId()));


        boolean overlap = reservationRepository.existsOverlappingReservation(facility.getId(), start, end);

        if (overlap) {
            throw new ConflictException("Reservation overlaps with existing reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFacility(facility);
        reservation.setStartTime(start);
        reservation.setEndTime(end);

        Reservation saved =  reservationRepository.save(reservation);
        return toResponse(saved);

    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getFacility().getId(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );
    }

    public Page<ReservationResponse> list(Long facilityId, Long userId, Pageable pageable) {
        if (facilityId == null && userId == null) {
            return reservationRepository.findByCanceledFalse(pageable).map(this::toResponse);
        }
        if (facilityId != null && userId == null) {
            return reservationRepository.findByFacilityIdAndCanceledFalse(facilityId, pageable).map(this::toResponse);
        }
        if (facilityId == null) {
            return reservationRepository.findByUserIdAndCanceledFalse(userId, pageable).map(this::toResponse);
        }

        return reservationRepository.findByFacilityIdAndUserIdAndCanceledFalse(facilityId, userId, pageable).map(this::toResponse);



    }

    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        return toResponse(reservation);
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest request) {

        LocalDateTime newStartTime = request.startTime();
        LocalDateTime newEndTime = request.endTime();

        if (!newEndTime.isAfter(newStartTime)) {
            throw new BadRequestException("endTime must be after startTime");
        }


        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        Long facilityId = reservation.getFacility().getId();

        boolean overlap = reservationRepository.existsOverlappingReservationExcludingId(
                facilityId,
                reservation.getId(),
                newStartTime,
                newEndTime
        );

        if (overlap) {
            throw new ConflictException("Reservation overlaps with existing reservation");
        }

        reservation.setStartTime(newStartTime);
        reservation.setEndTime(newEndTime);

        Reservation saved =  reservationRepository.save(reservation);
        return toResponse(saved);
    }

    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));

        if (reservation.isCanceled()) {
            throw new ConflictException("Reservation is already canceled");
        }

        reservation.setCanceled(true);
        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    public Page<ReservationResponse> listMine(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email: " + email));

        return reservationRepository.findByUserIdAndCanceledFalse(user.getId(), pageable)
                .map(this::toResponse);
    }

}
