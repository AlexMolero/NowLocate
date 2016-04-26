package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Temperatura;
import com.mycompany.myapp.repository.TemperaturaRepository;
import com.mycompany.myapp.repository.search.TemperaturaSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TemperaturaResource REST controller.
 *
 * @see TemperaturaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TemperaturaResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_HORA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HORA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HORA_STR = dateTimeFormatter.format(DEFAULT_HORA);

    private static final Integer DEFAULT_TEMPERATURA = 1;
    private static final Integer UPDATED_TEMPERATURA = 2;

    @Inject
    private TemperaturaRepository temperaturaRepository;

    @Inject
    private TemperaturaSearchRepository temperaturaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTemperaturaMockMvc;

    private Temperatura temperatura;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TemperaturaResource temperaturaResource = new TemperaturaResource();
        ReflectionTestUtils.setField(temperaturaResource, "temperaturaSearchRepository", temperaturaSearchRepository);
        ReflectionTestUtils.setField(temperaturaResource, "temperaturaRepository", temperaturaRepository);
        this.restTemperaturaMockMvc = MockMvcBuilders.standaloneSetup(temperaturaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        temperatura = new Temperatura();
        temperatura.setHora(DEFAULT_HORA);
        temperatura.setTemperatura(DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    public void createTemperatura() throws Exception {
        int databaseSizeBeforeCreate = temperaturaRepository.findAll().size();

        // Create the Temperatura

        restTemperaturaMockMvc.perform(post("/api/temperaturas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(temperatura)))
                .andExpect(status().isCreated());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturas = temperaturaRepository.findAll();
        assertThat(temperaturas).hasSize(databaseSizeBeforeCreate + 1);
        Temperatura testTemperatura = temperaturas.get(temperaturas.size() - 1);
        assertThat(testTemperatura.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    public void getAllTemperaturas() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturas
        restTemperaturaMockMvc.perform(get("/api/temperaturas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(temperatura.getId().intValue())))
                .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA_STR)))
                .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA)));
    }

    @Test
    @Transactional
    public void getTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get the temperatura
        restTemperaturaMockMvc.perform(get("/api/temperaturas/{id}", temperatura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(temperatura.getId().intValue()))
            .andExpect(jsonPath("$.hora").value(DEFAULT_HORA_STR))
            .andExpect(jsonPath("$.temperatura").value(DEFAULT_TEMPERATURA));
    }

    @Test
    @Transactional
    public void getNonExistingTemperatura() throws Exception {
        // Get the temperatura
        restTemperaturaMockMvc.perform(get("/api/temperaturas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

		int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();

        // Update the temperatura
        temperatura.setHora(UPDATED_HORA);
        temperatura.setTemperatura(UPDATED_TEMPERATURA);

        restTemperaturaMockMvc.perform(put("/api/temperaturas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(temperatura)))
                .andExpect(status().isOk());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturas = temperaturaRepository.findAll();
        assertThat(temperaturas).hasSize(databaseSizeBeforeUpdate);
        Temperatura testTemperatura = temperaturas.get(temperaturas.size() - 1);
        assertThat(testTemperatura.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    public void deleteTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

		int databaseSizeBeforeDelete = temperaturaRepository.findAll().size();

        // Get the temperatura
        restTemperaturaMockMvc.perform(delete("/api/temperaturas/{id}", temperatura.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Temperatura> temperaturas = temperaturaRepository.findAll();
        assertThat(temperaturas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
