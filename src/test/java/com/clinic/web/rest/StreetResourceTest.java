package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Street;
import com.clinic.repository.StreetRepository;
import com.clinic.repository.search.StreetSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StreetResource REST controller.
 *
 * @see StreetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StreetResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";
    private static final String DEFAULT_DOCDOC_ID = "AAAAA";
    private static final String UPDATED_DOCDOC_ID = "BBBBB";

    private static final DateTime DEFAULT_LAST_UPDATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_UPDATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_UPDATE_STR = dateTimeFormatter.print(DEFAULT_LAST_UPDATE);

    @Inject
    private StreetRepository streetRepository;

    @Inject
    private StreetSearchRepository streetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStreetMockMvc;

    private Street street;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StreetResource streetResource = new StreetResource();
        ReflectionTestUtils.setField(streetResource, "streetRepository", streetRepository);
        ReflectionTestUtils.setField(streetResource, "streetSearchRepository", streetSearchRepository);
        this.restStreetMockMvc = MockMvcBuilders.standaloneSetup(streetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        street = new Street();
        street.setName(DEFAULT_NAME);
        street.setAlias(DEFAULT_ALIAS);
        street.setDocdocId(DEFAULT_DOCDOC_ID);
        street.setLastUpdate(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createStreet() throws Exception {
        int databaseSizeBeforeCreate = streetRepository.findAll().size();

        // Create the Street

        restStreetMockMvc.perform(post("/api/streets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(street)))
                .andExpect(status().isCreated());

        // Validate the Street in the database
        List<Street> streets = streetRepository.findAll();
        assertThat(streets).hasSize(databaseSizeBeforeCreate + 1);
        Street testStreet = streets.get(streets.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStreet.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testStreet.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testStreet.getLastUpdate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = streetRepository.findAll().size();
        // set the field null
        street.setName(null);

        // Create the Street, which fails.

        restStreetMockMvc.perform(post("/api/streets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(street)))
                .andExpect(status().isBadRequest());

        List<Street> streets = streetRepository.findAll();
        assertThat(streets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = streetRepository.findAll().size();
        // set the field null
        street.setAlias(null);

        // Create the Street, which fails.

        restStreetMockMvc.perform(post("/api/streets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(street)))
                .andExpect(status().isBadRequest());

        List<Street> streets = streetRepository.findAll();
        assertThat(streets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStreets() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get all the streets
        restStreetMockMvc.perform(get("/api/streets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(street.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.toString())))
                .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE_STR)));
    }

    @Test
    @Transactional
    public void getStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", street.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(street.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.toString()))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingStreet() throws Exception {
        // Get the street
        restStreetMockMvc.perform(get("/api/streets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

		int databaseSizeBeforeUpdate = streetRepository.findAll().size();

        // Update the street
        street.setName(UPDATED_NAME);
        street.setAlias(UPDATED_ALIAS);
        street.setDocdocId(UPDATED_DOCDOC_ID);
        street.setLastUpdate(UPDATED_LAST_UPDATE);

        restStreetMockMvc.perform(put("/api/streets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(street)))
                .andExpect(status().isOk());

        // Validate the Street in the database
        List<Street> streets = streetRepository.findAll();
        assertThat(streets).hasSize(databaseSizeBeforeUpdate);
        Street testStreet = streets.get(streets.size() - 1);
        assertThat(testStreet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStreet.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testStreet.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testStreet.getLastUpdate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void deleteStreet() throws Exception {
        // Initialize the database
        streetRepository.saveAndFlush(street);

		int databaseSizeBeforeDelete = streetRepository.findAll().size();

        // Get the street
        restStreetMockMvc.perform(delete("/api/streets/{id}", street.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Street> streets = streetRepository.findAll();
        assertThat(streets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
