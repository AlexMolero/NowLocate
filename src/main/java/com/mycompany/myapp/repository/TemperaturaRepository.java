package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Temperatura;

import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Temperatura entity.
 */
public interface TemperaturaRepository extends JpaRepository<Temperatura,Long> {
    List<Temperatura> findAllByExpedicion(int expedicion);

}
