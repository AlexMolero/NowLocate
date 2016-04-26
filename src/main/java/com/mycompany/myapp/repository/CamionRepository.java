package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Camion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Camion entity.
 */
public interface CamionRepository extends JpaRepository<Camion,Long> {

}
