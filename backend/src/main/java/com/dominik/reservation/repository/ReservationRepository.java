package com.dominik.reservation.repository;

import com.dominik.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        select count(r) > 0
        from Reservation r
        where r.facility.id = :facilityId
            and :startTime < r.endTime
            and :endTime > r.startTime
            and r.canceled = false
    """)
    boolean existsOverlappingReservation(
            @Param("facilityId") Long facilityId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
            );

    @Query("""
        select count(r) > 0
        from Reservation r
        where r.facility.id = :facilityId
            and r.id <> :reservationId
            and :startTime < r.endTime
            and :endTime > r.startTime
            and r.canceled = false
    """)
    boolean existsOverlappingReservationExcludingId(
            @Param("facilityId") Long facilityId,
            @Param("reservationId") Long reservationId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    Page<Reservation> findByCanceledFalse(Pageable pageable);

    Page<Reservation> findByFacilityIdAndCanceledFalse(Long facilityId, Pageable pageable);

    Page<Reservation> findByUserIdAndCanceledFalse(Long userId, Pageable pageable);

    Page<Reservation> findByFacilityIdAndUserIdAndCanceledFalse(Long facilityId, Long userId, Pageable pageable);

    long countByUserIdAndCanceledFalse(Long userId);
}
