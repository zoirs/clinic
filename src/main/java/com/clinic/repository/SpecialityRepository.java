package com.clinic.repository;

import com.clinic.domain.Metro;
import com.clinic.domain.Speciality;
import com.clinic.domain.Street;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Speciality entity.
 */
public interface SpecialityRepository extends JpaRepository<Speciality,Long> {

    Optional<Speciality> findOneByDocdocId(Long docdocId);

    Optional<Speciality> findOneByAlias(String speciality);

    List<Speciality> findAllByOrderByNameAsc();
}
