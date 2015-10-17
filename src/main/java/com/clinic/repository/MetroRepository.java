package com.clinic.repository;

import com.clinic.domain.Metro;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Metro entity.
 */
public interface MetroRepository extends JpaRepository<Metro,Long> {

}
