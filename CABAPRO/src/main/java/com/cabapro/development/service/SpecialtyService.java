package com.cabapro.development.service;

import com.cabapro.development.model.Specialty;
import com.cabapro.development.repository.SpecialtyRepository;
import com.cabapro.development.service.RefereeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final RefereeService refereeService;

    public SpecialtyService(SpecialtyRepository specialtyRepository,
                            RefereeService refereeService) {
        this.specialtyRepository = specialtyRepository;
        this.refereeService = refereeService;
    }

    public List<Specialty> findAll() {
        return specialtyRepository.findAllByOrderByNameAsc();
    }

    public Specialty findById(Long id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found: " + id));
    }

    @Transactional
    public Specialty create(Specialty s) {
        validate(s);
        if (specialtyRepository.existsByNameIgnoreCase(s.getName()))
            throw new IllegalArgumentException("Specialty already exists with name: " + s.getName());
        s.setIdSpecialty(null);
        return specialtyRepository.save(s);
    }

    @Transactional
    public Specialty update(Long id, Specialty s) {
        Specialty existing = findById(id);
        validate(s);
        if (!existing.getName().equalsIgnoreCase(s.getName())
                && specialtyRepository.existsByNameIgnoreCase(s.getName())) {
            throw new IllegalArgumentException("Specialty already exists with name: " + s.getName());
        }
        existing.setName(s.getName());
        existing.setDescription(s.getDescription());
        return specialtyRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        if (refereeService.countBySpecialty(id) > 0) {
            throw new IllegalStateException("Cannot delete: specialty is assigned to referees.");
        }
        specialtyRepository.deleteById(id);
    }

    private void validate(Specialty s) {
        if (s.getName() == null || s.getName().isBlank())
            throw new IllegalArgumentException("Name is required.");
        if (s.getName().length() > 100)
            throw new IllegalArgumentException("Name is too long (max 100).");
    }
}