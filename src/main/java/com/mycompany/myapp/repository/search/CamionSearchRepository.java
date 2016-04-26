package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Camion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Camion entity.
 */
public interface CamionSearchRepository extends ElasticsearchRepository<Camion, Long> {
}
