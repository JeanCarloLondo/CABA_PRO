package com.cabapro.development.repository;
import com.cabapro.development.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;   

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    // Custom query methods (if needed) can be defined here
}