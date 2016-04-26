package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Delegacion;
import com.mycompany.myapp.repository.DelegacionRepository;
import com.mycompany.myapp.repository.search.DelegacionSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DelegacionResource REST controller.
 *
 * @see DelegacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DelegacionResourceIntTest {

    private static final String DEFAULT_LOCALIDAD = "AAAAA";
    private static final String UPDATED_LOCALIDAD = "BBBBB";

    private static final Integer DEFAULT_VOLUMEN_ALMACEN = 1;
    private static final Integer UPDATED_VOLUMEN_ALMACEN = 2;
    private static final String DEFAULT_CALLE = "AAAAA";
    private static final String UPDATED_CALLE = "BBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;

    @Inject
    private DelegacionRepository delegacionRepository;

    @Inject
    private DelegacionSearchRepository delegacionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDelegacionMockMvc;

    private Delegacion delegacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DelegacionResource delegacionResource = new DelegacionResource();
        ReflectionTestUtils.setField(delegacionResource, "delegacionSearchRepository", delegacionSearchRepository);
        ReflectionTestUtils.setField(delegacionResource, "delegacionRepository", delegacionRepository);
        this.restDelegacionMockMvc = MockMvcBuilders.standaloneSetup(delegacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        delegacion = new Delegacion();
        delegacion.setLocalidad(DEFAULT_LOCALIDAD);
        delegacion.setVolumenAlmacen(DEFAULT_VOLUMEN_ALMACEN);
        delegacion.setCalle(DEFAULT_CALLE);
        delegacion.setLatitud(DEFAULT_LATITUD);
        delegacion.setLongitud(DEFAULT_LONGITUD);
    }

    @Test
    @Transactional
    public void createDelegacion() throws Exception {
        int databaseSizeBeforeCreate = delegacionRepository.findAll().size();

        // Create the Delegacion

        restDelegacionMockMvc.perform(post("/api/delegacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegacion)))
                .andExpect(status().isCreated());

        // Validate the Delegacion in the database
        List<Delegacion> delegacions = delegacionRepository.findAll();
        assertThat(delegacions).hasSize(databaseSizeBeforeCreate + 1);
        Delegacion testDelegacion = delegacions.get(delegacions.size() - 1);
        assertThat(testDelegacion.getLocalidad()).isEqualTo(DEFAULT_LOCALIDAD);
        assertThat(testDelegacion.getVolumenAlmacen()).isEqualTo(DEFAULT_VOLUMEN_ALMACEN);
        assertThat(testDelegacion.getCalle()).isEqualTo(DEFAULT_CALLE);
        assertThat(testDelegacion.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testDelegacion.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
    }

    @Test
    @Transactional
    public void getAllDelegacions() throws Exception {
        // Initialize the database
        delegacionRepository.saveAndFlush(delegacion);

        // Get all the delegacions
        restDelegacionMockMvc.perform(get("/api/delegacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(delegacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].localidad").value(hasItem(DEFAULT_LOCALIDAD.toString())))
                .andExpect(jsonPath("$.[*].volumenAlmacen").value(hasItem(DEFAULT_VOLUMEN_ALMACEN)))
                .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE.toString())))
                .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
                .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())));
    }

    @Test
    @Transactional
    public void getDelegacion() throws Exception {
        // Initialize the database
        delegacionRepository.saveAndFlush(delegacion);

        // Get the delegacion
        restDelegacionMockMvc.perform(get("/api/delegacions/{id}", delegacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(delegacion.getId().intValue()))
            .andExpect(jsonPath("$.localidad").value(DEFAULT_LOCALIDAD.toString()))
            .andExpect(jsonPath("$.volumenAlmacen").value(DEFAULT_VOLUMEN_ALMACEN))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.doubleValue()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDelegacion() throws Exception {
        // Get the delegacion
        restDelegacionMockMvc.perform(get("/api/delegacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelegacion() throws Exception {
        // Initialize the database
        delegacionRepository.saveAndFlush(delegacion);

		int databaseSizeBeforeUpdate = delegacionRepository.findAll().size();

        // Update the delegacion
        delegacion.setLocalidad(UPDATED_LOCALIDAD);
        delegacion.setVolumenAlmacen(UPDATED_VOLUMEN_ALMACEN);
        delegacion.setCalle(UPDATED_CALLE);
        delegacion.setLatitud(UPDATED_LATITUD);
        delegacion.setLongitud(UPDATED_LONGITUD);

        restDelegacionMockMvc.perform(put("/api/delegacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegacion)))
                .andExpect(status().isOk());

        // Validate the Delegacion in the database
        List<Delegacion> delegacions = delegacionRepository.findAll();
        assertThat(delegacions).hasSize(databaseSizeBeforeUpdate);
        Delegacion testDelegacion = delegacions.get(delegacions.size() - 1);
        assertThat(testDelegacion.getLocalidad()).isEqualTo(UPDATED_LOCALIDAD);
        assertThat(testDelegacion.getVolumenAlmacen()).isEqualTo(UPDATED_VOLUMEN_ALMACEN);
        assertThat(testDelegacion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testDelegacion.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testDelegacion.getLongitud()).isEqualTo(UPDATED_LONGITUD);
    }

    @Test
    @Transactional
    public void deleteDelegacion() throws Exception {
        // Initialize the database
        delegacionRepository.saveAndFlush(delegacion);

		int databaseSizeBeforeDelete = delegacionRepository.findAll().size();

        // Get the delegacion
        restDelegacionMockMvc.perform(delete("/api/delegacions/{id}", delegacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Delegacion> delegacions = delegacionRepository.findAll();
        assertThat(delegacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
