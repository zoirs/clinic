package com.clinic.repository.search;

import com.clinic.domain.Clinic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Clinic entity.
 */
public interface ClinicSearchRepository extends ElasticsearchRepository<Clinic, Long> {
}
