package com.clinic.repository.search;

import com.clinic.domain.Street;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Street entity.
 */
public interface StreetSearchRepository extends ElasticsearchRepository<Street, Long> {
}
