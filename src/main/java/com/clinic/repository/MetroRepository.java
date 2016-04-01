package com.clinic.repository;

import com.clinic.domain.Metro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Metro entity.
 */
public interface MetroRepository extends JpaRepository<Metro, Long> {

    Optional<Metro> findOneByDocdocId(Long docdocId);

    @Query("SELECT m FROM Metro m left join fetch m.nearest n where m.alias = ?1")
    List<Metro> findByAliasWithNearest(String alias); // todo return одну запись

    List<Metro> findByCity_alias(String alias);  // todo

}
