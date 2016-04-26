package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Camion;
import com.mycompany.myapp.repository.CamionRepository;
import com.mycompany.myapp.repository.search.CamionSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Camion.
 */
@RestController
@RequestMapping("/api")
public class CamionResource {

    private final Logger log = LoggerFactory.getLogger(CamionResource.class);
        
    @Inject
    private CamionRepository camionRepository;
    
    @Inject
    private CamionSearchRepository camionSearchRepository;
    
    /**
     * POST  /camions -> Create a new camion.
     */
    @RequestMapping(value = "/camions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Camion> createCamion(@RequestBody Camion camion) throws URISyntaxException {
        log.debug("REST request to save Camion : {}", camion);
        if (camion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("camion", "idexists", "A new camion cannot already have an ID")).body(null);
        }
        Camion result = camionRepository.save(camion);
        camionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/camions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("camion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /camions -> Updates an existing camion.
     */
    @RequestMapping(value = "/camions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Camion> updateCamion(@RequestBody Camion camion) throws URISyntaxException {
        log.debug("REST request to update Camion : {}", camion);
        if (camion.getId() == null) {
            return createCamion(camion);
        }
        Camion result = camionRepository.save(camion);
        camionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("camion", camion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /camions -> get all the camions.
     */
    @RequestMapping(value = "/camions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Camion>> getAllCamions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Camions");
        Page<Camion> page = camionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/camions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /camions/:id -> get the "id" camion.
     */
    @RequestMapping(value = "/camions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Camion> getCamion(@PathVariable Long id) {
        log.debug("REST request to get Camion : {}", id);
        Camion camion = camionRepository.findOne(id);
        return Optional.ofNullable(camion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /camions/:id -> delete the "id" camion.
     */
    @RequestMapping(value = "/camions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCamion(@PathVariable Long id) {
        log.debug("REST request to delete Camion : {}", id);
        camionRepository.delete(id);
        camionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("camion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/camions/:query -> search for the camion corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/camions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Camion> searchCamions(@PathVariable String query) {
        log.debug("REST request to search Camions for query {}", query);
        return StreamSupport
            .stream(camionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
