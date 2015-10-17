package com.clinic.repository;

import com.clinic.domain.City;
import com.clinic.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the City entity.
 */
public interface CityRepository extends JpaRepository<City,Long> {

    Optional<City> findOneByDocdocId(Long docdocId);

}
