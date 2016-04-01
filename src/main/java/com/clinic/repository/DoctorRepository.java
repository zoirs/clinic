package com.clinic.repository;

import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import com.clinic.domain.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
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

    @Query("SELECT d FROM Doctor d INNER JOIN d.metros m  INNER JOIN d.specialitys s where m in (?1) and  s in (?2)")
    Page<Doctor> findByMetrosAndSpecialities(Collection<Metro> metros, Collection<Speciality> specialities, Pageable pageable);

    @Query("SELECT d FROM Doctor d INNER JOIN d.metros m where m in (?1)")
    Page<Doctor> findByMetros(Collection<Metro> metros, Pageable pageable);

    // todo сделать вьюшки для вытягиваемых объектов типа (SELECT c.name, c.capital.name FROM Country c)
    @Query("SELECT d FROM Doctor d INNER JOIN d.specialitys s where s in (?1)")
    Page<Doctor> findBySpecialities(Collection<Speciality> specialities, Pageable pageable);

}
