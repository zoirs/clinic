package com.clinic.repository.search;

import com.clinic.domain.Diagnostic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Diagnostic entity.
 */
public interface DiagnosticSearchRepository extends ElasticsearchRepository<Diagnostic, Long> {
}
