package com.clinic.repository.search;

import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Doctor entity.
 */
public interface DoctorSearchRepository extends ElasticsearchRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> findByMetros(Pageable pageable);

    // List<Test> findByUsers_UserName(String userName)

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"fio\" : {\"query\" : \"?*\",\"analyze_wildcard\" : true}}}}}")
    Page<Doctor> findByFioLike(String metro, Pageable pageable);

    Page<Doctor> findByMetros_Alias(String metro, Pageable pageable);

    Page<Doctor> findByMetros(Metro metro, Pageable pageable);


}
