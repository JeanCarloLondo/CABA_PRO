package com.cabapro.development.service;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.repository.RankingRepository;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RefereeRepository refereeRepository;

    public RankingService(RankingRepository rankingRepository,
                          RefereeRepository refereeRepository) {
        this.rankingRepository = rankingRepository;
        this.refereeRepository = refereeRepository;
    }

    /** Listar todos */
    public List<Ranking> findAll() {
        return rankingRepository.findAll();
    }

    /** Obtener por id (o 404 lógico) */
    public Ranking findById(Long id) {
        return rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ranking not found with id: " + id));
    }

    /** Crear */
    public Ranking create(Ranking ranking) {
        validateRequired(ranking);
        String normalized = ranking.getName().trim();

        if (rankingRepository.existsByNameIgnoreCase(normalized)) {
            throw new IllegalArgumentException("A ranking with that name already exists.");
        }

        ranking.setId(null); // asegurar creación
        ranking.setName(normalized);
        ranking.setDescription(ranking.getDescription().trim());
        return rankingRepository.save(ranking);
    }

    /** Actualizar */
    public Ranking update(Long id, Ranking updated) {
        validateRequired(updated);

        Ranking existing = findById(id);
        String newName = updated.getName().trim();

        // Si cambia el nombre, verificar unicidad
        if (!existing.getName().equalsIgnoreCase(newName)
                && rankingRepository.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("A ranking with that name already exists.");
        }

        existing.setName(newName);
        existing.setDescription(updated.getDescription().trim());
        return rankingRepository.save(existing);
    }

    /** Eliminar (bloqueado si hay referees asociados) */
    public void deleteById(Long id) {
        // valida existencia
        findById(id);

        long usage = refereeRepository.countByRankingId(id);
        if (usage > 0) {
            throw new IllegalStateException(
                    "Cannot delete ranking: it is associated with " + usage + " referee(s).");
        }
        rankingRepository.deleteById(id);
    }

    /** Validaciones obligatorias */
    private void validateRequired(Ranking ranking) {
        if (ranking == null) {
            throw new IllegalArgumentException("Ranking data is required.");
        }
        if (!StringUtils.hasText(ranking.getName())) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (!StringUtils.hasText(ranking.getDescription())) {
            throw new IllegalArgumentException("Description is required.");
        }
    }
}