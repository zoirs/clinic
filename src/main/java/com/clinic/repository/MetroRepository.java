package com.clinic.repository;

import com.clinic.domain.Metro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Metro entity.
 */
public interface MetroRepository extends JpaRepository<Metro, Long> {

    Optional<Metro> findOneByDocdocId(Long docdocId);
    Optional<Metro> findOneByAlias(String alias);

}
