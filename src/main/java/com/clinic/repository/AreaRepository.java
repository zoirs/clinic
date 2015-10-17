package com.clinic.repository;

import com.clinic.domain.Area;
import com.clinic.domain.Street;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Area entity.
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findOneByDocdocId(Long docdocId);

}
