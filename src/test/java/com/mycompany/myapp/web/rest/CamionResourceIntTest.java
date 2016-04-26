package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Camion;
import com.mycompany.myapp.repository.CamionRepository;
import com.mycompany.myapp.repository.search.CamionSearchRepository;

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
 * Test class for the CamionResource REST controller.
 *
 * @see CamionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CamionResourceIntTest {

    private static final String DEFAULT_MATRICULA = "AAAAA";
    private static final String UPDATED_MATRICULA = "BBBBB";

    private static final Integer DEFAULT_VOLUMEN = 1;
    private static final Integer UPDATED_VOLUMEN = 2;

    @Inject
    private CamionRepository camionRepository;

    @Inject
    private CamionSearchRepository camionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCamionMockMvc;

    private Camion camion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CamionResource camionResource = new CamionResource();
        ReflectionTestUtils.setField(camionResource, "camionSearchRepository", camionSearchRepository);
        ReflectionTestUtils.setField(camionResource, "camionRepository", camionRepository);
        this.restCamionMockMvc = MockMvcBuilders.standaloneSetup(camionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        camion = new Camion();
        camion.setMatricula(DEFAULT_MATRICULA);
        camion.setVolumen(DEFAULT_VOLUMEN);
    }

    @Test
    @Transactional
    public void createCamion() throws Exception {
        int databaseSizeBeforeCreate = camionRepository.findAll().size();

        // Create the Camion

        restCamionMockMvc.perform(post("/api/camions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(camion)))
                .andExpect(status().isCreated());

        // Validate the Camion in the database
        List<Camion> camions = camionRepository.findAll();
        assertThat(camions).hasSize(databaseSizeBeforeCreate + 1);
        Camion testCamion = camions.get(camions.size() - 1);
        assertThat(testCamion.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testCamion.getVolumen()).isEqualTo(DEFAULT_VOLUMEN);
    }

    @Test
    @Transactional
    public void getAllCamions() throws Exception {
        // Initialize the database
        camionRepository.saveAndFlush(camion);

        // Get all the camions
        restCamionMockMvc.perform(get("/api/camions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(camion.getId().intValue())))
                .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA.toString())))
                .andExpect(jsonPath("$.[*].volumen").value(hasItem(DEFAULT_VOLUMEN)));
    }

    @Test
    @Transactional
    public void getCamion() throws Exception {
        // Initialize the database
        camionRepository.saveAndFlush(camion);

        // Get the camion
        restCamionMockMvc.perform(get("/api/camions/{id}", camion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(camion.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA.toString()))
            .andExpect(jsonPath("$.volumen").value(DEFAULT_VOLUMEN));
    }

    @Test
    @Transactional
    public void getNonExistingCamion() throws Exception {
        // Get the camion
        restCamionMockMvc.perform(get("/api/camions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCamion() throws Exception {
        // Initialize the database
        camionRepository.saveAndFlush(camion);

		int databaseSizeBeforeUpdate = camionRepository.findAll().size();

        // Update the camion
        camion.setMatricula(UPDATED_MATRICULA);
        camion.setVolumen(UPDATED_VOLUMEN);

        restCamionMockMvc.perform(put("/api/camions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(camion)))
                .andExpect(status().isOk());

        // Validate the Camion in the database
        List<Camion> camions = camionRepository.findAll();
        assertThat(camions).hasSize(databaseSizeBeforeUpdate);
        Camion testCamion = camions.get(camions.size() - 1);
        assertThat(testCamion.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testCamion.getVolumen()).isEqualTo(UPDATED_VOLUMEN);
    }

    @Test
    @Transactional
    public void deleteCamion() throws Exception {
        // Initialize the database
        camionRepository.saveAndFlush(camion);

		int databaseSizeBeforeDelete = camionRepository.findAll().size();

        // Get the camion
        restCamionMockMvc.perform(delete("/api/camions/{id}", camion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Camion> camions = camionRepository.findAll();
        assertThat(camions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
