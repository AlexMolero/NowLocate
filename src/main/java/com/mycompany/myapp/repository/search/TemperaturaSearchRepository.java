package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Temperatura;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Temperatura entity.
 */
public interface TemperaturaSearchRepository extends ElasticsearchRepository<Temperatura, Long> {
}
