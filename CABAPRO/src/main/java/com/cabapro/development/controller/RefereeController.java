/**
 * REST controller for managing Referee entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Controller - Referee (Admin only)
 */

package com.cabapro.development.controller;

import com.cabapro.development.model.Referee;
import com.cabapro.development.service.RefereeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/referees")
public class RefereeController {

    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @GetMapping
    public ResponseEntity<List<Referee>> getAll() {
        return ResponseEntity.ok(refereeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(refereeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Referee> create(@RequestBody Referee referee) {
        return ResponseEntity.ok(refereeService.save(referee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Referee> update(@PathVariable Long id, @RequestBody Referee referee) {
        Referee existing = refereeService.findById(id);
        existing.setRanking(referee.getRanking());
        return ResponseEntity.ok(refereeService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        refereeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}