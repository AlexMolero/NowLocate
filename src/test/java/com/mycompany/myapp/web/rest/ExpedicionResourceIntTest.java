package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Expedicion;
import com.mycompany.myapp.repository.ExpedicionRepository;
import com.mycompany.myapp.repository.search.ExpedicionSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ExpedicionResource REST controller.
 *
 * @see ExpedicionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExpedicionResourceIntTest {


    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_FRIGORIFICO = false;
    private static final Boolean UPDATED_FRIGORIFICO = true;

    private static final Integer DEFAULT_TEMP_MAX = 1;
    private static final Integer UPDATED_TEMP_MAX = 2;

    private static final Integer DEFAULT_TEMP_MIN = 1;
    private static final Integer UPDATED_TEMP_MIN = 2;
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private ExpedicionRepository expedicionRepository;

    @Inject
    private ExpedicionSearchRepository expedicionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExpedicionMockMvc;

    private Expedicion expedicion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpedicionResource expedicionResource = new ExpedicionResource();
        ReflectionTestUtils.setField(expedicionResource, "expedicionSearchRepository", expedicionSearchRepository);
        ReflectionTestUtils.setField(expedicionResource, "expedicionRepository", expedicionRepository);
        this.restExpedicionMockMvc = MockMvcBuilders.standaloneSetup(expedicionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        expedicion = new Expedicion();
        expedicion.setFechaInicio(DEFAULT_FECHA_INICIO);
        expedicion.setFechaEntrega(DEFAULT_FECHA_ENTREGA);
        expedicion.setFrigorifico(DEFAULT_FRIGORIFICO);
        expedicion.setTempMax(DEFAULT_TEMP_MAX);
        expedicion.setTempMin(DEFAULT_TEMP_MIN);
        expedicion.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createExpedicion() throws Exception {
        int databaseSizeBeforeCreate = expedicionRepository.findAll().size();

        // Create the Expedicion

        restExpedicionMockMvc.perform(post("/api/expedicions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expedicion)))
                .andExpect(status().isCreated());

        // Validate the Expedicion in the database
        List<Expedicion> expedicions = expedicionRepository.findAll();
        assertThat(expedicions).hasSize(databaseSizeBeforeCreate + 1);
        Expedicion testExpedicion = expedicions.get(expedicions.size() - 1);
        assertThat(testExpedicion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testExpedicion.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testExpedicion.getFrigorifico()).isEqualTo(DEFAULT_FRIGORIFICO);
        assertThat(testExpedicion.getTempMax()).isEqualTo(DEFAULT_TEMP_MAX);
        assertThat(testExpedicion.getTempMin()).isEqualTo(DEFAULT_TEMP_MIN);
        assertThat(testExpedicion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllExpedicions() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

        // Get all the expedicions
        restExpedicionMockMvc.perform(get("/api/expedicions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expedicion.getId().intValue())))
                .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
                .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].frigorifico").value(hasItem(DEFAULT_FRIGORIFICO.booleanValue())))
                .andExpect(jsonPath("$.[*].tempMax").value(hasItem(DEFAULT_TEMP_MAX)))
                .andExpect(jsonPath("$.[*].tempMin").value(hasItem(DEFAULT_TEMP_MIN)))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

        // Get the expedicion
        restExpedicionMockMvc.perform(get("/api/expedicions/{id}", expedicion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expedicion.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.frigorifico").value(DEFAULT_FRIGORIFICO.booleanValue()))
            .andExpect(jsonPath("$.tempMax").value(DEFAULT_TEMP_MAX))
            .andExpect(jsonPath("$.tempMin").value(DEFAULT_TEMP_MIN))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpedicion() throws Exception {
        // Get the expedicion
        restExpedicionMockMvc.perform(get("/api/expedicions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

		int databaseSizeBeforeUpdate = expedicionRepository.findAll().size();

        // Update the expedicion
        expedicion.setFechaInicio(UPDATED_FECHA_INICIO);
        expedicion.setFechaEntrega(UPDATED_FECHA_ENTREGA);
        expedicion.setFrigorifico(UPDATED_FRIGORIFICO);
        expedicion.setTempMax(UPDATED_TEMP_MAX);
        expedicion.setTempMin(UPDATED_TEMP_MIN);
        expedicion.setDescripcion(UPDATED_DESCRIPCION);

        restExpedicionMockMvc.perform(put("/api/expedicions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expedicion)))
                .andExpect(status().isOk());

        // Validate the Expedicion in the database
        List<Expedicion> expedicions = expedicionRepository.findAll();
        assertThat(expedicions).hasSize(databaseSizeBeforeUpdate);
        Expedicion testExpedicion = expedicions.get(expedicions.size() - 1);
        assertThat(testExpedicion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testExpedicion.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testExpedicion.getFrigorifico()).isEqualTo(UPDATED_FRIGORIFICO);
        assertThat(testExpedicion.getTempMax()).isEqualTo(UPDATED_TEMP_MAX);
        assertThat(testExpedicion.getTempMin()).isEqualTo(UPDATED_TEMP_MIN);
        assertThat(testExpedicion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteExpedicion() throws Exception {
        // Initialize the database
        expedicionRepository.saveAndFlush(expedicion);

		int databaseSizeBeforeDelete = expedicionRepository.findAll().size();

        // Get the expedicion
        restExpedicionMockMvc.perform(delete("/api/expedicions/{id}", expedicion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expedicion> expedicions = expedicionRepository.findAll();
        assertThat(expedicions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
