package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Diagnostic;
import com.clinic.repository.DiagnosticRepository;
import com.clinic.repository.search.DiagnosticSearchRepository;

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
 * Test class for the DiagnosticResource REST controller.
 *
 * @see DiagnosticResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DiagnosticResourceTest {

    private static final java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);


    @Inject
    private DiagnosticRepository diagnosticRepository;

    @Inject
    private DiagnosticSearchRepository diagnosticSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiagnosticMockMvc;

    private Diagnostic diagnostic;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiagnosticResource diagnosticResource = new DiagnosticResource();
        ReflectionTestUtils.setField(diagnosticResource, "diagnosticRepository", diagnosticRepository);
        ReflectionTestUtils.setField(diagnosticResource, "diagnosticSearchRepository", diagnosticSearchRepository);
        this.restDiagnosticMockMvc = MockMvcBuilders.standaloneSetup(diagnosticResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        diagnostic = new Diagnostic();
        diagnostic.setName(DEFAULT_NAME);
        diagnostic.setAlias(DEFAULT_ALIAS);
        diagnostic.setDocdocId(DEFAULT_DOCDOC_ID);
        diagnostic.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createDiagnostic() throws Exception {
        int databaseSizeBeforeCreate = diagnosticRepository.findAll().size();

        // Create the Diagnostic

        restDiagnosticMockMvc.perform(post("/api/diagnostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
                .andExpect(status().isCreated());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnostics = diagnosticRepository.findAll();
        assertThat(diagnostics).hasSize(databaseSizeBeforeCreate + 1);
        Diagnostic testDiagnostic = diagnostics.get(diagnostics.size() - 1);
        assertThat(testDiagnostic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDiagnostic.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testDiagnostic.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testDiagnostic.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticRepository.findAll().size();
        // set the field null
        diagnostic.setName(null);

        // Create the Diagnostic, which fails.

        restDiagnosticMockMvc.perform(post("/api/diagnostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
                .andExpect(status().isBadRequest());

        List<Diagnostic> diagnostics = diagnosticRepository.findAll();
        assertThat(diagnostics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = diagnosticRepository.findAll().size();
        // set the field null
        diagnostic.setAlias(null);

        // Create the Diagnostic, which fails.

        restDiagnosticMockMvc.perform(post("/api/diagnostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
                .andExpect(status().isBadRequest());

        List<Diagnostic> diagnostics = diagnosticRepository.findAll();
        assertThat(diagnostics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiagnostics() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnostics
        restDiagnosticMockMvc.perform(get("/api/diagnostics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostic.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get the diagnostic
        restDiagnosticMockMvc.perform(get("/api/diagnostics/{id}", diagnostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(diagnostic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDiagnostic() throws Exception {
        // Get the diagnostic
        restDiagnosticMockMvc.perform(get("/api/diagnostics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

		int databaseSizeBeforeUpdate = diagnosticRepository.findAll().size();

        // Update the diagnostic
        diagnostic.setName(UPDATED_NAME);
        diagnostic.setAlias(UPDATED_ALIAS);
        diagnostic.setDocdocId(UPDATED_DOCDOC_ID);
        diagnostic.setLastUpdated(UPDATED_LAST_UPDATED);

        restDiagnosticMockMvc.perform(put("/api/diagnostics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diagnostic)))
                .andExpect(status().isOk());

        // Validate the Diagnostic in the database
        List<Diagnostic> diagnostics = diagnosticRepository.findAll();
        assertThat(diagnostics).hasSize(databaseSizeBeforeUpdate);
        Diagnostic testDiagnostic = diagnostics.get(diagnostics.size() - 1);
        assertThat(testDiagnostic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDiagnostic.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testDiagnostic.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testDiagnostic.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

		int databaseSizeBeforeDelete = diagnosticRepository.findAll().size();

        // Get the diagnostic
        restDiagnosticMockMvc.perform(delete("/api/diagnostics/{id}", diagnostic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Diagnostic> diagnostics = diagnosticRepository.findAll();
        assertThat(diagnostics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
