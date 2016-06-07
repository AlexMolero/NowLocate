package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Expedicion;
import com.mycompany.myapp.domain.Temperatura;
import com.mycompany.myapp.repository.ExpedicionRepository;
import com.mycompany.myapp.repository.TemperaturaRepository;
import com.mycompany.myapp.repository.search.ExpedicionSearchRepository;
import com.mycompany.myapp.service.MailService;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Expedicion.
 */
@RestController
@RequestMapping("/api")
public class ExpedicionResource {

    private final Logger log = LoggerFactory.getLogger(ExpedicionResource.class);

    @Inject
    private ExpedicionRepository expedicionRepository;

    @Inject
    private ExpedicionSearchRepository expedicionSearchRepository;

    @Inject
    private MailService mailService;

    @Inject
    private TemperaturaRepository temperaturaRepository;

    /**
     * POST  /expedicions -> Create a new expedicion.
     */
    @RequestMapping(value = "/expedicions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expedicion> createExpedicion(@RequestBody Expedicion expedicion) throws URISyntaxException {
        log.debug("REST request to save Expedicion : {}", expedicion);
        if (expedicion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expedicion", "idexists", "A new expedicion cannot already have an ID")).body(null);
        }
        Expedicion result = expedicionRepository.save(expedicion);
        //INICIO:Crea un registro de temperaturas segun la cantidad de dias.
        //START:Create a new temperature record according to the number of days.

        createTemperatures(result);

        //FIN:Crea un registro de temperaturas segun la cantidad de dias.
        //END:Create a new temperature record according to the number of days.

        expedicionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/expedicions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expedicion", result.getId().toString()))
            .body(result);
    }

    private void createTemperatures(Expedicion result) {
        int tempMax = (result.getTempMax()+1);
        int tempMin = (result.getTempMin()-1);
        LocalDate endDate = result.getFechaEntrega();
        LocalDate startDate = result.getFechaInicio();
        long days = Period.between(startDate, endDate).getDays();
        long entradasTemp = days*2;
        for(int i=0;i<entradasTemp;i++) {
                int random = ((int)(Math.random()*(tempMax-tempMin))+tempMin);
                if(random>result.getTempMax() ){
                    String averia = "La temperatura de la expedicion con id: " +
                        result.getId() + " a sobrepasado el pico de temperatura adecuado en: " + tempMax +
                        " cuando el maximo esta establecido en: " + result.getTempMax();
                    mailService.sendWarningTemperature(averia);
                }
                if(random<result.getTempMin()){
                    String averia = "La temperatura de la expedicion con id:" +
                        result.getId() + " a disminuido la temperatura adecuado en: " + tempMin +
                        " cuando el minimo establecido es: " + result.getTempMin();
                     mailService.sendWarningTemperature(averia);
                }
                Temperatura temperatura = new Temperatura();
                temperatura.setExpedicion(result);
                temperatura.setTemperatura(random);
                temperaturaRepository.save(temperatura);
            }
    }

    /**
     * PUT  /expedicions -> Updates an existing expedicion.
     */
    @RequestMapping(value = "/expedicions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expedicion> updateExpedicion(@RequestBody Expedicion expedicion) throws URISyntaxException {
        log.debug("REST request to update Expedicion : {}", expedicion);
        if (expedicion.getId() == null) {
            return createExpedicion(expedicion);
        }
        Expedicion result = expedicionRepository.save(expedicion);
        expedicionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expedicion", expedicion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expedicions -> get all the expedicions.
     */
    @RequestMapping(value = "/expedicions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Expedicion>> getAllExpedicions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Expedicions");
        Page<Expedicion> page = expedicionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expedicions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expedicions/:id -> get the "id" expedicion.
     */
    @RequestMapping(value = "/expedicions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expedicion> getExpedicion(@PathVariable Long id) {

        /* Envio de email
        mailService.sendEmail("amoleron@gmail.com", "prueba","olakease",false,false);
         */

        log.debug("REST request to get Expedicion : {}", id);
        Expedicion expedicion = expedicionRepository.findOne(id);
        return Optional.ofNullable(expedicion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expedicions/:id -> delete the "id" expedicion.
     */
    @RequestMapping(value = "/expedicions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpedicion(@PathVariable Long id) {
        log.debug("REST request to delete Expedicion : {}", id);

        //temperaturaRepository.deleteByExpedicion(id);

        expedicionRepository.delete(id);
        expedicionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expedicion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/expedicions/:query -> search for the expedicion corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/expedicions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Expedicion> searchExpedicions(@PathVariable String query) {
        log.debug("REST request to search Expedicions for query {}", query);
        return StreamSupport
            .stream(expedicionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    /*
    * ola
    * */
    @RequestMapping(value = "/expedicions/{id}/temperaturas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List <Temperatura>> getTemperaturas(@PathVariable Long id) {

         List<Temperatura> temperatura = temperaturaRepository.findAllByExpedicionId(id);

        return Optional.ofNullable(temperatura)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
