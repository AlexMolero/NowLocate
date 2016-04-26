package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Expedicion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Expedicion entity.
 */
public interface ExpedicionRepository extends JpaRepository<Expedicion,Long> {

}
