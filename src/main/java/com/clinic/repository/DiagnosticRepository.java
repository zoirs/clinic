package com.clinic.repository;

import com.clinic.domain.Diagnostic;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Diagnostic entity.
 */
public interface DiagnosticRepository extends JpaRepository<Diagnostic,Long> {

}
