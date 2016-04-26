package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Delegacion;
import com.mycompany.myapp.repository.DelegacionRepository;
import com.mycompany.myapp.repository.search.DelegacionSearchRepository;
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
 * REST controller for managing Delegacion.
 */
@RestController
@RequestMapping("/api")
public class DelegacionResource {

    private final Logger log = LoggerFactory.getLogger(DelegacionResource.class);
        
    @Inject
    private DelegacionRepository delegacionRepository;
    
    @Inject
    private DelegacionSearchRepository delegacionSearchRepository;
    
    /**
     * POST  /delegacions -> Create a new delegacion.
     */
    @RequestMapping(value = "/delegacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegacion> createDelegacion(@RequestBody Delegacion delegacion) throws URISyntaxException {
        log.debug("REST request to save Delegacion : {}", delegacion);
        if (delegacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("delegacion", "idexists", "A new delegacion cannot already have an ID")).body(null);
        }
        Delegacion result = delegacionRepository.save(delegacion);
        delegacionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/delegacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("delegacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /delegacions -> Updates an existing delegacion.
     */
    @RequestMapping(value = "/delegacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegacion> updateDelegacion(@RequestBody Delegacion delegacion) throws URISyntaxException {
        log.debug("REST request to update Delegacion : {}", delegacion);
        if (delegacion.getId() == null) {
            return createDelegacion(delegacion);
        }
        Delegacion result = delegacionRepository.save(delegacion);
        delegacionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("delegacion", delegacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /delegacions -> get all the delegacions.
     */
    @RequestMapping(value = "/delegacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Delegacion>> getAllDelegacions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Delegacions");
        Page<Delegacion> page = delegacionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/delegacions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /delegacions/:id -> get the "id" delegacion.
     */
    @RequestMapping(value = "/delegacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegacion> getDelegacion(@PathVariable Long id) {
        log.debug("REST request to get Delegacion : {}", id);
        Delegacion delegacion = delegacionRepository.findOne(id);
        return Optional.ofNullable(delegacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /delegacions/:id -> delete the "id" delegacion.
     */
    @RequestMapping(value = "/delegacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDelegacion(@PathVariable Long id) {
        log.debug("REST request to delete Delegacion : {}", id);
        delegacionRepository.delete(id);
        delegacionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("delegacion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/delegacions/:query -> search for the delegacion corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/delegacions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Delegacion> searchDelegacions(@PathVariable String query) {
        log.debug("REST request to search Delegacions for query {}", query);
        return StreamSupport
            .stream(delegacionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
