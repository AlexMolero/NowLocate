package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Delegacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Delegacion entity.
 */
public interface DelegacionSearchRepository extends ElasticsearchRepository<Delegacion, Long> {
}
