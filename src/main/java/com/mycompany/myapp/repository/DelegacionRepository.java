package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Delegacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Delegacion entity.
 */
public interface DelegacionRepository extends JpaRepository<Delegacion,Long> {

}
