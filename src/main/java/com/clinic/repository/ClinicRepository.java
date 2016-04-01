package com.clinic.repository;

import com.clinic.domain.Area;
import com.clinic.domain.Clinic;
import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import com.clinic.domain.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Clinic entity.
 */
public interface ClinicRepository extends JpaRepository<Clinic,Long> {

    @Query("select distinct clinic from Clinic clinic left join fetch clinic.diagnostics left join fetch clinic.metros left join fetch clinic.specialitys")
    List<Clinic> findAllWithEagerRelationships();

    @Query("select clinic from Clinic clinic left join fetch clinic.diagnostics left join fetch clinic.metros left join fetch clinic.specialitys where clinic.id =:id")
    Clinic findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Clinic> findOneByDocdocId(Long docdocId);

    @Query("SELECT c FROM Clinic c INNER JOIN c.metros m  INNER JOIN c.specialitys s where m in (?1) and  s in (?2)")
    Page<Clinic> findByMetrosAndSpecialities(Collection<Metro> metros, Collection<Speciality> specialities, Pageable pageable);

    @Query("SELECT c FROM Clinic c INNER JOIN c.metros m where m in (?1)")
    Page<Clinic> findByMetros(Collection<Metro> metros, Pageable pageable);

    @Query("SELECT c FROM Clinic c INNER JOIN c.specialitys s where s in (?1)")
    Page<Clinic> findBySpecialities(Collection<Speciality> specialities, Pageable pageable);

}
