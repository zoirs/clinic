package com.clinic.repository;

import com.clinic.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    @Query("select distinct doctor from Doctor doctor left join fetch doctor.clinics left join fetch doctor.specialitys left join fetch doctor.metros")
    List<Doctor> findAllWithEagerRelationships();

    @Query("select doctor from Doctor doctor left join fetch doctor.clinics left join fetch doctor.specialitys left join fetch doctor.metros where doctor.id =:id")
    Doctor findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Doctor> findOneByDocdocId(Long docdocId);

}
