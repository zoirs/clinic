package com.clinic.repository;

import com.clinic.domain.Area;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
public interface AreaRepository extends JpaRepository<Area,Long> {

}
