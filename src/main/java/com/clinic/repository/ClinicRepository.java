package com.clinic.repository;

import com.clinic.domain.Clinic;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Clinic entity.
 */
public interface ClinicRepository extends JpaRepository<Clinic,Long> {

    @Query("select distinct clinic from Clinic clinic left join fetch clinic.diagnostics left join fetch clinic.metros left join fetch clinic.specialitys")
    List<Clinic> findAllWithEagerRelationships();

    @Query("select clinic from Clinic clinic left join fetch clinic.diagnostics left join fetch clinic.metros left join fetch clinic.specialitys where clinic.id =:id")
    Clinic findOneWithEagerRelationships(@Param("id") Long id);

}
