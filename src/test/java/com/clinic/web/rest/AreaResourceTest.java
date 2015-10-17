package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Area;
import com.clinic.repository.AreaRepository;
import com.clinic.repository.search.AreaSearchRepository;

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
 * Test class for the AreaResource REST controller.
 *
 * @see AreaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AreaResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final DateTime DEFAULT_LAST_UPDATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_UPDATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.print(DEFAULT_LAST_UPDATED);

    @Inject
    private AreaRepository areaRepository;

    @Inject
    private AreaSearchRepository areaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAreaMockMvc;

    private Area area;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AreaResource areaResource = new AreaResource();
        ReflectionTestUtils.setField(areaResource, "areaRepository", areaRepository);
        ReflectionTestUtils.setField(areaResource, "areaSearchRepository", areaSearchRepository);
        this.restAreaMockMvc = MockMvcBuilders.standaloneSetup(areaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        area = new Area();
        area.setName(DEFAULT_NAME);
        area.setAlias(DEFAULT_ALIAS);
        area.setDocdocId(DEFAULT_DOCDOC_ID);
        area.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createArea() throws Exception {
        int databaseSizeBeforeCreate = areaRepository.findAll().size();

        // Create the Area

        restAreaMockMvc.perform(post("/api/areas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(area)))
                .andExpect(status().isCreated());

        // Validate the Area in the database
        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(databaseSizeBeforeCreate + 1);
        Area testArea = areas.get(areas.size() - 1);
        assertThat(testArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArea.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testArea.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testArea.getLastUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setName(null);

        // Create the Area, which fails.

        restAreaMockMvc.perform(post("/api/areas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(area)))
                .andExpect(status().isBadRequest());

        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setAlias(null);

        // Create the Area, which fails.

        restAreaMockMvc.perform(post("/api/areas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(area)))
                .andExpect(status().isBadRequest());

        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAreas() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

        // Get all the areas
        restAreaMockMvc.perform(get("/api/areas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(area.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

        // Get the area
        restAreaMockMvc.perform(get("/api/areas/{id}", area.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(area.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingArea() throws Exception {
        // Get the area
        restAreaMockMvc.perform(get("/api/areas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

		int databaseSizeBeforeUpdate = areaRepository.findAll().size();

        // Update the area
        area.setName(UPDATED_NAME);
        area.setAlias(UPDATED_ALIAS);
        area.setDocdocId(UPDATED_DOCDOC_ID);
        area.setLastUpdated(UPDATED_LAST_UPDATED);

        restAreaMockMvc.perform(put("/api/areas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(area)))
                .andExpect(status().isOk());

        // Validate the Area in the database
        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(databaseSizeBeforeUpdate);
        Area testArea = areas.get(areas.size() - 1);
        assertThat(testArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArea.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testArea.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testArea.getLastUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

		int databaseSizeBeforeDelete = areaRepository.findAll().size();

        // Get the area
        restAreaMockMvc.perform(delete("/api/areas/{id}", area.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
