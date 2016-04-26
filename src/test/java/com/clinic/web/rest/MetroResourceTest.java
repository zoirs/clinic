package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Metro;
import com.clinic.repository.MetroRepository;
import com.clinic.repository.search.MetroSearchRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MetroResource REST controller.
 *
 * @see MetroResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MetroResourceTest {

    private static final java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";
    private static final String DEFAULT_LINE_NAME = "AAAAA";
    private static final String UPDATED_LINE_NAME = "BBBBB";
    private static final String DEFAULT_LINE_COLOR = "AAAAA";
    private static final String UPDATED_LINE_COLOR = "BBBBB";

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);

    @Inject
    private MetroRepository metroRepository;

    @Inject
    private MetroSearchRepository metroSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMetroMockMvc;

    private Metro metro;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetroResource metroResource = new MetroResource();
        ReflectionTestUtils.setField(metroResource, "metroRepository", metroRepository);
        ReflectionTestUtils.setField(metroResource, "metroSearchRepository", metroSearchRepository);
        this.restMetroMockMvc = MockMvcBuilders.standaloneSetup(metroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metro = new Metro();
        metro.setName(DEFAULT_NAME);
        metro.setAlias(DEFAULT_ALIAS);
        metro.setLineName(DEFAULT_LINE_NAME);
        metro.setLineColor(DEFAULT_LINE_COLOR);
        metro.setDocdocId(DEFAULT_DOCDOC_ID);
        metro.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createMetro() throws Exception {
        int databaseSizeBeforeCreate = metroRepository.findAll().size();

        // Create the Metro

        restMetroMockMvc.perform(post("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isCreated());

        // Validate the Metro in the database
        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeCreate + 1);
        Metro testMetro = metros.get(metros.size() - 1);
        assertThat(testMetro.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMetro.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testMetro.getLineName()).isEqualTo(DEFAULT_LINE_NAME);
        assertThat(testMetro.getLineColor()).isEqualTo(DEFAULT_LINE_COLOR);
        assertThat(testMetro.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testMetro.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setName(null);

        // Create the Metro, which fails.

        restMetroMockMvc.perform(post("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isBadRequest());

        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setAlias(null);

        // Create the Metro, which fails.

        restMetroMockMvc.perform(post("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isBadRequest());

        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLineNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setLineName(null);

        // Create the Metro, which fails.

        restMetroMockMvc.perform(post("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isBadRequest());

        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLineColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = metroRepository.findAll().size();
        // set the field null
        metro.setLineColor(null);

        // Create the Metro, which fails.

        restMetroMockMvc.perform(post("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isBadRequest());

        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetros() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get all the metros
        restMetroMockMvc.perform(get("/api/metros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metro.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].lineName").value(hasItem(DEFAULT_LINE_NAME.toString())))
                .andExpect(jsonPath("$.[*].lineColor").value(hasItem(DEFAULT_LINE_COLOR.toString())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

        // Get the metro
        restMetroMockMvc.perform(get("/api/metros/{id}", metro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metro.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.lineName").value(DEFAULT_LINE_NAME.toString()))
            .andExpect(jsonPath("$.lineColor").value(DEFAULT_LINE_COLOR.toString()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMetro() throws Exception {
        // Get the metro
        restMetroMockMvc.perform(get("/api/metros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

		int databaseSizeBeforeUpdate = metroRepository.findAll().size();

        // Update the metro
        metro.setName(UPDATED_NAME);
        metro.setAlias(UPDATED_ALIAS);
        metro.setLineName(UPDATED_LINE_NAME);
        metro.setLineColor(UPDATED_LINE_COLOR);
        metro.setDocdocId(UPDATED_DOCDOC_ID);
        metro.setLastUpdated(UPDATED_LAST_UPDATED);

        restMetroMockMvc.perform(put("/api/metros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metro)))
                .andExpect(status().isOk());

        // Validate the Metro in the database
        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeUpdate);
        Metro testMetro = metros.get(metros.size() - 1);
        assertThat(testMetro.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMetro.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testMetro.getLineName()).isEqualTo(UPDATED_LINE_NAME);
        assertThat(testMetro.getLineColor()).isEqualTo(UPDATED_LINE_COLOR);
        assertThat(testMetro.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testMetro.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteMetro() throws Exception {
        // Initialize the database
        metroRepository.saveAndFlush(metro);

		int databaseSizeBeforeDelete = metroRepository.findAll().size();

        // Get the metro
        restMetroMockMvc.perform(delete("/api/metros/{id}", metro.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Metro> metros = metroRepository.findAll();
        assertThat(metros).hasSize(databaseSizeBeforeDelete - 1);
    }
}
