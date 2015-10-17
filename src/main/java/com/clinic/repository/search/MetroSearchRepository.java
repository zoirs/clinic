package com.clinic.repository.search;

import com.clinic.domain.Metro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Metro entity.
 */
public interface MetroSearchRepository extends ElasticsearchRepository<Metro, Long> {
}
