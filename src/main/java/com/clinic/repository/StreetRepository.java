package com.clinic.repository;

import com.clinic.domain.Street;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Street entity.
 */
public interface StreetRepository extends JpaRepository<Street,Long> {

    Optional<Street> findOneByDocdocId(Long docdocId);

}
