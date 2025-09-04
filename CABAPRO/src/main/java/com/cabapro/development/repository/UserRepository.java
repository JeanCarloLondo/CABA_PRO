package com.cabapro.development.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cabapro.development.model.CustomUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
}
