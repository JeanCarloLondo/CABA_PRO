/**
 * REST controller for managing Ranking entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Controller - Ranking (Admin only)
 */
package com.cabapro.development.controller;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public ResponseEntity<List<Ranking>> getAll() {
        return ResponseEntity.ok(rankingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ranking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rankingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Ranking> create(@RequestBody Ranking ranking) {
        return ResponseEntity.ok(rankingService.save(ranking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ranking> update(@PathVariable Long id, @RequestBody Ranking ranking) {
        Ranking existing = rankingService.findById(id);
        existing.setName(ranking.getName());
        existing.setFee(ranking.getFee());
        existing.setDescription(ranking.getDescription());
        return ResponseEntity.ok(rankingService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rankingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}