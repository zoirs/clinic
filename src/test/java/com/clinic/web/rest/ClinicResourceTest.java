package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Clinic;
import com.clinic.repository.ClinicRepository;
import com.clinic.repository.search.ClinicSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ClinicResource REST controller.
 *
 * @see ClinicResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClinicResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SHORT_NAME = "AAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final String DEFAULT_STREET_NAME = "AAAAA";
    private static final String UPDATED_STREET_NAME = "BBBBB";
    private static final String DEFAULT_HOUSE = "AAAAA";
    private static final String UPDATED_HOUSE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_WEEKDAYS_OPEN = "AAAAA";
    private static final String UPDATED_WEEKDAYS_OPEN = "BBBBB";
    private static final String DEFAULT_WEEKEND_OPEN = "AAAAA";
    private static final String UPDATED_WEEKEND_OPEN = "BBBBB";
    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_IS_DIAGNOSTIC = false;
    private static final Boolean UPDATED_IS_DIAGNOSTIC = true;

    private static final Boolean DEFAULT_IS_CLINIC = false;
    private static final Boolean UPDATED_IS_CLINIC = true;

    private static final Boolean DEFAULT_IS_DOCTOR = false;
    private static final Boolean UPDATED_IS_DOCTOR = true;
    private static final String DEFAULT_PHONE_CONTACT = "AAAAA";
    private static final String UPDATED_PHONE_CONTACT = "BBBBB";
    private static final String DEFAULT_PHONE_APPOINTMEN = "AAAAA";
    private static final String UPDATED_PHONE_APPOINTMEN = "BBBBB";
    private static final String DEFAULT_PHONE_REPLACEMENT = "AAAAA";
    private static final String UPDATED_PHONE_REPLACEMENT = "BBBBB";
    private static final String DEFAULT_LOGO_PATH = "AAAAA";
    private static final String UPDATED_LOGO_PATH = "BBBBB";
    private static final String DEFAULT_LOGO = "AAAAA";
    private static final String UPDATED_LOGO = "BBBBB";

    private static final Boolean DEFAULT_SCHEDULE_STATE_ONLINE = false;
    private static final Boolean UPDATED_SCHEDULE_STATE_ONLINE = true;
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final BigDecimal DEFAULT_MIN_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MIN_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MAX_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final DateTime DEFAULT_LAST_UPDATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_LAST_UPDATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.print(DEFAULT_LAST_UPDATED);

    @Inject
    private ClinicRepository clinicRepository;

    @Inject
    private ClinicSearchRepository clinicSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClinicMockMvc;

    private Clinic clinic;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClinicResource clinicResource = new ClinicResource();
        ReflectionTestUtils.setField(clinicResource, "clinicRepository", clinicRepository);
        ReflectionTestUtils.setField(clinicResource, "clinicSearchRepository", clinicSearchRepository);
        this.restClinicMockMvc = MockMvcBuilders.standaloneSetup(clinicResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clinic = new Clinic();
        clinic.setName(DEFAULT_NAME);
        clinic.setShortName(DEFAULT_SHORT_NAME);
        clinic.setAlias(DEFAULT_ALIAS);
        clinic.setUrl(DEFAULT_URL);
        clinic.setLongitude(DEFAULT_LONGITUDE);
        clinic.setLatitude(DEFAULT_LATITUDE);
        clinic.setStreetName(DEFAULT_STREET_NAME);
        clinic.setHouse(DEFAULT_HOUSE);
        clinic.setDescription(DEFAULT_DESCRIPTION);
        clinic.setWeekdaysOpen(DEFAULT_WEEKDAYS_OPEN);
        clinic.setWeekendOpen(DEFAULT_WEEKEND_OPEN);
        clinic.setShortDescription(DEFAULT_SHORT_DESCRIPTION);
        clinic.setIsDiagnostic(DEFAULT_IS_DIAGNOSTIC);
        clinic.setIsClinic(DEFAULT_IS_CLINIC);
        clinic.setIsDoctor(DEFAULT_IS_DOCTOR);
        clinic.setPhoneContact(DEFAULT_PHONE_CONTACT);
        clinic.setPhoneAppointmen(DEFAULT_PHONE_APPOINTMEN);
        clinic.setPhoneReplacement(DEFAULT_PHONE_REPLACEMENT);
        clinic.setLogoPath(DEFAULT_LOGO_PATH);
        clinic.setLogo(DEFAULT_LOGO);
        clinic.setScheduleStateOnline(DEFAULT_SCHEDULE_STATE_ONLINE);
        clinic.setEmail(DEFAULT_EMAIL);
        clinic.setMinPrice(DEFAULT_MIN_PRICE);
        clinic.setMaxPrice(DEFAULT_MAX_PRICE);
        clinic.setDocdocId(DEFAULT_DOCDOC_ID);
        clinic.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createClinic() throws Exception {
        int databaseSizeBeforeCreate = clinicRepository.findAll().size();

        // Create the Clinic

        restClinicMockMvc.perform(post("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isCreated());

        // Validate the Clinic in the database
        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeCreate + 1);
        Clinic testClinic = clinics.get(clinics.size() - 1);
        assertThat(testClinic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClinic.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testClinic.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testClinic.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testClinic.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testClinic.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testClinic.getStreetName()).isEqualTo(DEFAULT_STREET_NAME);
        assertThat(testClinic.getHouse()).isEqualTo(DEFAULT_HOUSE);
        assertThat(testClinic.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClinic.getWeekdaysOpen()).isEqualTo(DEFAULT_WEEKDAYS_OPEN);
        assertThat(testClinic.getWeekendOpen()).isEqualTo(DEFAULT_WEEKEND_OPEN);
        assertThat(testClinic.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testClinic.getIsDiagnostic()).isEqualTo(DEFAULT_IS_DIAGNOSTIC);
        assertThat(testClinic.getIsClinic()).isEqualTo(DEFAULT_IS_CLINIC);
        assertThat(testClinic.getIsDoctor()).isEqualTo(DEFAULT_IS_DOCTOR);
        assertThat(testClinic.getPhoneContact()).isEqualTo(DEFAULT_PHONE_CONTACT);
        assertThat(testClinic.getPhoneAppointmen()).isEqualTo(DEFAULT_PHONE_APPOINTMEN);
        assertThat(testClinic.getPhoneReplacement()).isEqualTo(DEFAULT_PHONE_REPLACEMENT);
        assertThat(testClinic.getLogoPath()).isEqualTo(DEFAULT_LOGO_PATH);
        assertThat(testClinic.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testClinic.getScheduleStateOnline()).isEqualTo(DEFAULT_SCHEDULE_STATE_ONLINE);
        assertThat(testClinic.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClinic.getMinPrice()).isEqualTo(DEFAULT_MIN_PRICE);
        assertThat(testClinic.getMaxPrice()).isEqualTo(DEFAULT_MAX_PRICE);
        assertThat(testClinic.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testClinic.getLastUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicRepository.findAll().size();
        // set the field null
        clinic.setName(null);

        // Create the Clinic, which fails.

        restClinicMockMvc.perform(post("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isBadRequest());

        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicRepository.findAll().size();
        // set the field null
        clinic.setShortName(null);

        // Create the Clinic, which fails.

        restClinicMockMvc.perform(post("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isBadRequest());

        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicRepository.findAll().size();
        // set the field null
        clinic.setAlias(null);

        // Create the Clinic, which fails.

        restClinicMockMvc.perform(post("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isBadRequest());

        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = clinicRepository.findAll().size();
        // set the field null
        clinic.setLastUpdated(null);

        // Create the Clinic, which fails.

        restClinicMockMvc.perform(post("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isBadRequest());

        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClinics() throws Exception {
        // Initialize the database
        clinicRepository.saveAndFlush(clinic);

        // Get all the clinics
        restClinicMockMvc.perform(get("/api/clinics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clinic.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
                .andExpect(jsonPath("$.[*].house").value(hasItem(DEFAULT_HOUSE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].weekdaysOpen").value(hasItem(DEFAULT_WEEKDAYS_OPEN.toString())))
                .andExpect(jsonPath("$.[*].weekendOpen").value(hasItem(DEFAULT_WEEKEND_OPEN.toString())))
                .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].isDiagnostic").value(hasItem(DEFAULT_IS_DIAGNOSTIC.booleanValue())))
                .andExpect(jsonPath("$.[*].isClinic").value(hasItem(DEFAULT_IS_CLINIC.booleanValue())))
                .andExpect(jsonPath("$.[*].isDoctor").value(hasItem(DEFAULT_IS_DOCTOR.booleanValue())))
                .andExpect(jsonPath("$.[*].phoneContact").value(hasItem(DEFAULT_PHONE_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].phoneAppointmen").value(hasItem(DEFAULT_PHONE_APPOINTMEN.toString())))
                .andExpect(jsonPath("$.[*].phoneReplacement").value(hasItem(DEFAULT_PHONE_REPLACEMENT.toString())))
                .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH.toString())))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
                .andExpect(jsonPath("$.[*].scheduleStateOnline").value(hasItem(DEFAULT_SCHEDULE_STATE_ONLINE.booleanValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].minPrice").value(hasItem(DEFAULT_MIN_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].maxPrice").value(hasItem(DEFAULT_MAX_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getClinic() throws Exception {
        // Initialize the database
        clinicRepository.saveAndFlush(clinic);

        // Get the clinic
        restClinicMockMvc.perform(get("/api/clinics/{id}", clinic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clinic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.streetName").value(DEFAULT_STREET_NAME.toString()))
            .andExpect(jsonPath("$.house").value(DEFAULT_HOUSE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.weekdaysOpen").value(DEFAULT_WEEKDAYS_OPEN.toString()))
            .andExpect(jsonPath("$.weekendOpen").value(DEFAULT_WEEKEND_OPEN.toString()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isDiagnostic").value(DEFAULT_IS_DIAGNOSTIC.booleanValue()))
            .andExpect(jsonPath("$.isClinic").value(DEFAULT_IS_CLINIC.booleanValue()))
            .andExpect(jsonPath("$.isDoctor").value(DEFAULT_IS_DOCTOR.booleanValue()))
            .andExpect(jsonPath("$.phoneContact").value(DEFAULT_PHONE_CONTACT.toString()))
            .andExpect(jsonPath("$.phoneAppointmen").value(DEFAULT_PHONE_APPOINTMEN.toString()))
            .andExpect(jsonPath("$.phoneReplacement").value(DEFAULT_PHONE_REPLACEMENT.toString()))
            .andExpect(jsonPath("$.logoPath").value(DEFAULT_LOGO_PATH.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.scheduleStateOnline").value(DEFAULT_SCHEDULE_STATE_ONLINE.booleanValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.minPrice").value(DEFAULT_MIN_PRICE.intValue()))
            .andExpect(jsonPath("$.maxPrice").value(DEFAULT_MAX_PRICE.intValue()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingClinic() throws Exception {
        // Get the clinic
        restClinicMockMvc.perform(get("/api/clinics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClinic() throws Exception {
        // Initialize the database
        clinicRepository.saveAndFlush(clinic);

		int databaseSizeBeforeUpdate = clinicRepository.findAll().size();

        // Update the clinic
        clinic.setName(UPDATED_NAME);
        clinic.setShortName(UPDATED_SHORT_NAME);
        clinic.setAlias(UPDATED_ALIAS);
        clinic.setUrl(UPDATED_URL);
        clinic.setLongitude(UPDATED_LONGITUDE);
        clinic.setLatitude(UPDATED_LATITUDE);
        clinic.setStreetName(UPDATED_STREET_NAME);
        clinic.setHouse(UPDATED_HOUSE);
        clinic.setDescription(UPDATED_DESCRIPTION);
        clinic.setWeekdaysOpen(UPDATED_WEEKDAYS_OPEN);
        clinic.setWeekendOpen(UPDATED_WEEKEND_OPEN);
        clinic.setShortDescription(UPDATED_SHORT_DESCRIPTION);
        clinic.setIsDiagnostic(UPDATED_IS_DIAGNOSTIC);
        clinic.setIsClinic(UPDATED_IS_CLINIC);
        clinic.setIsDoctor(UPDATED_IS_DOCTOR);
        clinic.setPhoneContact(UPDATED_PHONE_CONTACT);
        clinic.setPhoneAppointmen(UPDATED_PHONE_APPOINTMEN);
        clinic.setPhoneReplacement(UPDATED_PHONE_REPLACEMENT);
        clinic.setLogoPath(UPDATED_LOGO_PATH);
        clinic.setLogo(UPDATED_LOGO);
        clinic.setScheduleStateOnline(UPDATED_SCHEDULE_STATE_ONLINE);
        clinic.setEmail(UPDATED_EMAIL);
        clinic.setMinPrice(UPDATED_MIN_PRICE);
        clinic.setMaxPrice(UPDATED_MAX_PRICE);
        clinic.setDocdocId(UPDATED_DOCDOC_ID);
        clinic.setLastUpdated(UPDATED_LAST_UPDATED);

        restClinicMockMvc.perform(put("/api/clinics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clinic)))
                .andExpect(status().isOk());

        // Validate the Clinic in the database
        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeUpdate);
        Clinic testClinic = clinics.get(clinics.size() - 1);
        assertThat(testClinic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClinic.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testClinic.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testClinic.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testClinic.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testClinic.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testClinic.getStreetName()).isEqualTo(UPDATED_STREET_NAME);
        assertThat(testClinic.getHouse()).isEqualTo(UPDATED_HOUSE);
        assertThat(testClinic.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClinic.getWeekdaysOpen()).isEqualTo(UPDATED_WEEKDAYS_OPEN);
        assertThat(testClinic.getWeekendOpen()).isEqualTo(UPDATED_WEEKEND_OPEN);
        assertThat(testClinic.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testClinic.getIsDiagnostic()).isEqualTo(UPDATED_IS_DIAGNOSTIC);
        assertThat(testClinic.getIsClinic()).isEqualTo(UPDATED_IS_CLINIC);
        assertThat(testClinic.getIsDoctor()).isEqualTo(UPDATED_IS_DOCTOR);
        assertThat(testClinic.getPhoneContact()).isEqualTo(UPDATED_PHONE_CONTACT);
        assertThat(testClinic.getPhoneAppointmen()).isEqualTo(UPDATED_PHONE_APPOINTMEN);
        assertThat(testClinic.getPhoneReplacement()).isEqualTo(UPDATED_PHONE_REPLACEMENT);
        assertThat(testClinic.getLogoPath()).isEqualTo(UPDATED_LOGO_PATH);
        assertThat(testClinic.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testClinic.getScheduleStateOnline()).isEqualTo(UPDATED_SCHEDULE_STATE_ONLINE);
        assertThat(testClinic.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClinic.getMinPrice()).isEqualTo(UPDATED_MIN_PRICE);
        assertThat(testClinic.getMaxPrice()).isEqualTo(UPDATED_MAX_PRICE);
        assertThat(testClinic.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testClinic.getLastUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteClinic() throws Exception {
        // Initialize the database
        clinicRepository.saveAndFlush(clinic);

		int databaseSizeBeforeDelete = clinicRepository.findAll().size();

        // Get the clinic
        restClinicMockMvc.perform(delete("/api/clinics/{id}", clinic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Clinic> clinics = clinicRepository.findAll();
        assertThat(clinics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
