/**
 * Repository interface for managing Admin entities.
 *
 * Author: Alejandro Garcés Ramírez
 * Date: 2025-09-03
 * Role: Data access layer - Admin
 */
package com.cabapro.development.repository;

import com.cabapro.development.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}