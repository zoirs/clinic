package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Speciality;
import com.clinic.repository.SpecialityRepository;
import com.clinic.repository.search.SpecialitySearchRepository;

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
 * Test class for the SpecialityResource REST controller.
 *
 * @see SpecialityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpecialityResourceTest {

    private static final java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";
    private static final String DEFAULT_NAME_GENITIVE = "AAAAA";
    private static final String UPDATED_NAME_GENITIVE = "BBBBB";
    private static final String DEFAULT_NAME_PLURAL = "AAAAA";
    private static final String UPDATED_NAME_PLURAL = "BBBBB";
    private static final String DEFAULT_NAME_PLURAL_GENITIVE = "AAAAA";
    private static final String UPDATED_NAME_PLURAL_GENITIVE = "BBBBB";

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);

    @Inject
    private SpecialityRepository specialityRepository;

    @Inject
    private SpecialitySearchRepository specialitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpecialityMockMvc;

    private Speciality speciality;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecialityResource specialityResource = new SpecialityResource();
        ReflectionTestUtils.setField(specialityResource, "specialityRepository", specialityRepository);
        ReflectionTestUtils.setField(specialityResource, "specialitySearchRepository", specialitySearchRepository);
        this.restSpecialityMockMvc = MockMvcBuilders.standaloneSetup(specialityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        speciality = new Speciality();
        speciality.setName(DEFAULT_NAME);
        speciality.setAlias(DEFAULT_ALIAS);
        speciality.setNameGenitive(DEFAULT_NAME_GENITIVE);
        speciality.setNamePlural(DEFAULT_NAME_PLURAL);
        speciality.setNamePluralGenitive(DEFAULT_NAME_PLURAL_GENITIVE);
        speciality.setDocdocId(DEFAULT_DOCDOC_ID);
        speciality.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createSpeciality() throws Exception {
        int databaseSizeBeforeCreate = specialityRepository.findAll().size();

        // Create the Speciality

        restSpecialityMockMvc.perform(post("/api/specialitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(speciality)))
                .andExpect(status().isCreated());

        // Validate the Speciality in the database
        List<Speciality> specialitys = specialityRepository.findAll();
        assertThat(specialitys).hasSize(databaseSizeBeforeCreate + 1);
        Speciality testSpeciality = specialitys.get(specialitys.size() - 1);
        assertThat(testSpeciality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpeciality.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testSpeciality.getNameGenitive()).isEqualTo(DEFAULT_NAME_GENITIVE);
        assertThat(testSpeciality.getNamePlural()).isEqualTo(DEFAULT_NAME_PLURAL);
        assertThat(testSpeciality.getNamePluralGenitive()).isEqualTo(DEFAULT_NAME_PLURAL_GENITIVE);
        assertThat(testSpeciality.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testSpeciality.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialityRepository.findAll().size();
        // set the field null
        speciality.setName(null);

        // Create the Speciality, which fails.

        restSpecialityMockMvc.perform(post("/api/specialitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(speciality)))
                .andExpect(status().isBadRequest());

        List<Speciality> specialitys = specialityRepository.findAll();
        assertThat(specialitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialityRepository.findAll().size();
        // set the field null
        speciality.setAlias(null);

        // Create the Speciality, which fails.

        restSpecialityMockMvc.perform(post("/api/specialitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(speciality)))
                .andExpect(status().isBadRequest());

        List<Speciality> specialitys = specialityRepository.findAll();
        assertThat(specialitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecialitys() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialitys
        restSpecialityMockMvc.perform(get("/api/specialitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(speciality.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].nameGenitive").value(hasItem(DEFAULT_NAME_GENITIVE.toString())))
                .andExpect(jsonPath("$.[*].namePlural").value(hasItem(DEFAULT_NAME_PLURAL.toString())))
                .andExpect(jsonPath("$.[*].namePluralGenitive").value(hasItem(DEFAULT_NAME_PLURAL_GENITIVE.toString())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get the speciality
        restSpecialityMockMvc.perform(get("/api/specialitys/{id}", speciality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(speciality.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.nameGenitive").value(DEFAULT_NAME_GENITIVE.toString()))
            .andExpect(jsonPath("$.namePlural").value(DEFAULT_NAME_PLURAL.toString()))
            .andExpect(jsonPath("$.namePluralGenitive").value(DEFAULT_NAME_PLURAL_GENITIVE.toString()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSpeciality() throws Exception {
        // Get the speciality
        restSpecialityMockMvc.perform(get("/api/specialitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

		int databaseSizeBeforeUpdate = specialityRepository.findAll().size();

        // Update the speciality
        speciality.setName(UPDATED_NAME);
        speciality.setAlias(UPDATED_ALIAS);
        speciality.setNameGenitive(UPDATED_NAME_GENITIVE);
        speciality.setNamePlural(UPDATED_NAME_PLURAL);
        speciality.setNamePluralGenitive(UPDATED_NAME_PLURAL_GENITIVE);
        speciality.setDocdocId(UPDATED_DOCDOC_ID);
        speciality.setLastUpdated(UPDATED_LAST_UPDATED);

        restSpecialityMockMvc.perform(put("/api/specialitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(speciality)))
                .andExpect(status().isOk());

        // Validate the Speciality in the database
        List<Speciality> specialitys = specialityRepository.findAll();
        assertThat(specialitys).hasSize(databaseSizeBeforeUpdate);
        Speciality testSpeciality = specialitys.get(specialitys.size() - 1);
        assertThat(testSpeciality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpeciality.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testSpeciality.getNameGenitive()).isEqualTo(UPDATED_NAME_GENITIVE);
        assertThat(testSpeciality.getNamePlural()).isEqualTo(UPDATED_NAME_PLURAL);
        assertThat(testSpeciality.getNamePluralGenitive()).isEqualTo(UPDATED_NAME_PLURAL_GENITIVE);
        assertThat(testSpeciality.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testSpeciality.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

		int databaseSizeBeforeDelete = specialityRepository.findAll().size();

        // Get the speciality
        restSpecialityMockMvc.perform(delete("/api/specialitys/{id}", speciality.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Speciality> specialitys = specialityRepository.findAll();
        assertThat(specialitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
