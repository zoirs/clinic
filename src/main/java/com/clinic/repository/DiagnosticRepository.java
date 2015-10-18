package com.clinic.repository;

import com.clinic.domain.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Diagnostic entity.
 */
public interface DiagnosticRepository extends JpaRepository<Diagnostic,Long> {

    Optional<Diagnostic> findOneByDocdocId(Long docdocId);

}
