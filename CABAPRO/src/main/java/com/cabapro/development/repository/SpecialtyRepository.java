package com.cabapro.development.repository;
import com.cabapro.development.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;   

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
}
