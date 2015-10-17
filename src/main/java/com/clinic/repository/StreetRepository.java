package com.clinic.repository;

import com.clinic.domain.Street;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Street entity.
 */
public interface StreetRepository extends JpaRepository<Street,Long> {

}
