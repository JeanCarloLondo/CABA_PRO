package com.cabapro.development.service;

import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Tournament;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepo;
    private final RefereeRepository refereeRepo;

    public TournamentService(TournamentRepository tournamentRepo,
                             RefereeRepository refereeRepo) {
        this.tournamentRepo = tournamentRepo;
        this.refereeRepo = refereeRepo;
    }

    public List<Tournament> findAll() {
        return tournamentRepo.findAll();
    }

    public Tournament findById(Long id) {
        return tournamentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + id));
    }

    @Transactional
    public Tournament create(String name, String teams, String rounds) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (tournamentRepo.existsByNameIgnoreCase(name.trim())) {
            throw new IllegalArgumentException("Tournament name already exists: " + name);
        }
        Tournament t = new Tournament();
        t.setName(name.trim());
        t.setTeams(teams);
        t.setRounds(rounds);
        return tournamentRepo.save(t);
    }

    @Transactional
    public Tournament update(Long id, String name, String teams, String rounds) {
        Tournament t = findById(id);
        String newName = name == null ? "" : name.trim();
        if (newName.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (!t.getName().equalsIgnoreCase(newName) && tournamentRepo.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Tournament name already exists: " + newName);
        }
        t.setName(newName);
        t.setTeams(teams);
        t.setRounds(rounds);
        return tournamentRepo.save(t);
    }

    @Transactional
    public void delete(Long id) {
        Tournament t = findById(id);
        // regla: puedes impedir borrar si tiene partidos
        // if (!t.getMatches().isEmpty()) throw new IllegalArgumentException("Cannot delete: tournament has matches");
        tournamentRepo.delete(t);
    }

    // ---------- Asignación de árbitros ----------
    public Set<Referee> assignedReferees(Long tournamentId) {
        return new HashSet<>(findById(tournamentId).getReferees());
    }

    public List<Referee> unassignedReferees(Long tournamentId) {
        Set<Long> assignedIds = assignedReferees(tournamentId).stream()
                .map(Referee::getId)
                .collect(Collectors.toSet());
        return refereeRepo.findAll().stream()
                .filter(r -> !assignedIds.contains(r.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addReferee(Long tournamentId, Long refereeId) {
        Tournament t = findById(tournamentId);
        Referee r = refereeRepo.findById(refereeId)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + refereeId));
        t.getReferees().add(r);
        tournamentRepo.save(t);
    }

    @Transactional
    public void removeReferee(Long tournamentId, Long refereeId) {
        Tournament t = findById(tournamentId);
        t.getReferees().removeIf(r -> Objects.equals(r.getId(), refereeId));
        tournamentRepo.save(t);
    }

    public List<Tournament> findActive(LocalDateTime since) {
        return tournamentRepo.findActiveSince(since);
    }
}