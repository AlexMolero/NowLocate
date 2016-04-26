package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Expedicion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Expedicion entity.
 */
public interface ExpedicionSearchRepository extends ElasticsearchRepository<Expedicion, Long> {
}
