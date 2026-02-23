package com.dominik.reservation.repository;

import com.dominik.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
