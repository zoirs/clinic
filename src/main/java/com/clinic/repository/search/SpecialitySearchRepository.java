package com.clinic.repository.search;

import com.clinic.domain.Speciality;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Speciality entity.
 */
public interface SpecialitySearchRepository extends ElasticsearchRepository<Speciality, Long> {
}
