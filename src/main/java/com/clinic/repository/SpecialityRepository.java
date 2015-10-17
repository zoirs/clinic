package com.clinic.repository;

import com.clinic.domain.Speciality;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Speciality entity.
 */
public interface SpecialityRepository extends JpaRepository<Speciality,Long> {

}
