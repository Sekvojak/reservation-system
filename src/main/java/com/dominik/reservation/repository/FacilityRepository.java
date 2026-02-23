package com.dominik.reservation.repository;

import com.dominik.reservation.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    boolean existsByName(String name);
}
