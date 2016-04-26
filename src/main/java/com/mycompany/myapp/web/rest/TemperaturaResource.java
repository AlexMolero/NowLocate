package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Temperatura;
import com.mycompany.myapp.repository.TemperaturaRepository;
import com.mycompany.myapp.repository.search.TemperaturaSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.LocalDate;
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
 * REST controller for managing Temperatura.
 */
@RestController
@RequestMapping("/api")
public class TemperaturaResource {

    private final Logger log = LoggerFactory.getLogger(TemperaturaResource.class);

    @Inject
    private TemperaturaRepository temperaturaRepository;

    @Inject
    private TemperaturaSearchRepository temperaturaSearchRepository;

    /**
     * POST  /temperaturas -> Create a new temperatura.
     */
    @RequestMapping(value = "/temperaturas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temperatura> createTemperatura(@RequestBody Temperatura temperatura) throws URISyntaxException {
        log.debug("REST request to save Temperatura : {}", temperatura);
        if (temperatura.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("temperatura", "idexists", "A new temperatura cannot already have an ID")).body(null);
        }
        Temperatura result = temperaturaRepository.save(temperatura);
        temperaturaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/temperaturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("temperatura", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /temperaturas -> Updates an existing temperatura.
     */
    @RequestMapping(value = "/temperaturas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temperatura> updateTemperatura(@RequestBody Temperatura temperatura) throws URISyntaxException {
        log.debug("REST request to update Temperatura : {}", temperatura);
        if (temperatura.getId() == null) {
            return createTemperatura(temperatura);
        }
        Temperatura result = temperaturaRepository.save(temperatura);
        temperaturaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("temperatura", temperatura.getId().toString()))
            .body(result);
    }

    /**
     * GET  /temperaturas -> get all the temperaturas.
     */
    @RequestMapping(value = "/temperaturas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Temperatura>> getAllTemperaturas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Temperaturas");
        Page<Temperatura> page = temperaturaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/temperaturas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
/**
 * Temperatura by Expedicion*//*

@RequestMapping(value = "/bp-by-days/{expedicion}")
@Timed
public ResponseEntity<Temperatura> getByDays(@PathVariable int expedicion) {
    /*LocalDate today = new LocalDate();
    LocalDate previousDate = today.minusDays(days);
    DateTime daysAgo = previousDate.toDateTimeAtCurrentTime();
    DateTime rightNow = today.toDateTimeAtCurrentTime();*/
/*
    List<Temperatura> readings = TemperaturaRepository.findAllByExpedicion(expedicion);
    //Temperatura response = new Temperatura("Last " + days + " Days", readings);
    Temperatura response = new Temperatura();
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    /**
     * GET  /temperaturas/:id -> get the "id" temperatura.
     */
    @RequestMapping(value = "/temperaturas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Temperatura> getTemperatura(@PathVariable Long id) {
        log.debug("REST request to get Temperatura : {}", id);
        Temperatura temperatura = temperaturaRepository.findOne(id);
        return Optional.ofNullable(temperatura)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /temperaturas/:id -> delete the "id" temperatura.
     */
    @RequestMapping(value = "/temperaturas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTemperatura(@PathVariable Long id) {
        log.debug("REST request to delete Temperatura : {}", id);
        temperaturaRepository.delete(id);
        temperaturaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("temperatura", id.toString())).build();
    }

    /**
     * SEARCH  /_search/temperaturas/:query -> search for the temperatura corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/temperaturas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Temperatura> searchTemperaturas(@PathVariable String query) {
        log.debug("REST request to search Temperaturas for query {}", query);
        return StreamSupport
            .stream(temperaturaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
