package com.dominik.reservation.repository;

import com.dominik.reservation.entity.Reservation;
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
            and :endTime > r.startTime\s
   \s""")
    boolean existsOverlappingReservation(
            @Param("facilityId") Long facilityId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
            );


}
