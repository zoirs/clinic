package com.clinic.web.rest;

import com.clinic.Application;
import com.clinic.domain.Doctor;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.search.DoctorSearchRepository;

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
 * Test class for the DoctorResource REST controller.
 *
 * @see DoctorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DoctorResourceTest {

    private static final java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final String DEFAULT_FIO4 = "AAAAA";
    private static final String UPDATED_FIO4 = "BBBBB";
    private static final String DEFAULT_ALIAS = "AAAAA";
    private static final String UPDATED_ALIAS = "BBBBB";

    private static final Float DEFAULT_RATING = 0F;
    private static final Float UPDATED_RATING = 1F;

    private static final Float DEFAULT_RATING_INTERNAL = 0F;
    private static final Float UPDATED_RATING_INTERNAL = 1F;

    private static final Integer DEFAULT_PRICE_FIRST = 1;
    private static final Integer UPDATED_PRICE_FIRST = 2;

    private static final Integer DEFAULT_PRICE_SPECIAL = 1;
    private static final Integer UPDATED_PRICE_SPECIAL = 2;

    private static final Integer DEFAULT_SEX = 1;
    private static final Integer UPDATED_SEX = 2;
    private static final String DEFAULT_IMG = "AAAAA";
    private static final String UPDATED_IMG = "BBBBB";

    private static final Integer DEFAULT_REVIEW_COUNT = 1;
    private static final Integer UPDATED_REVIEW_COUNT = 2;
    private static final String DEFAULT_TEXT_ABOUT = "AAAAA";
    private static final String UPDATED_TEXT_ABOUT = "BBBBB";

    private static final Integer DEFAULT_EXPERIENCA_YEAR = 1;
    private static final Integer UPDATED_EXPERIENCA_YEAR = 2;

    private static final Boolean DEFAULT_DEPARTURE = false;
    private static final Boolean UPDATED_DEPARTURE = true;
    private static final String DEFAULT_CATEGORY = "AAAAA";
    private static final String UPDATED_CATEGORY = "BBBBB";
    private static final String DEFAULT_DEGREE = "AAAAA";
    private static final String UPDATED_DEGREE = "BBBBB";
    private static final String DEFAULT_RANK = "AAAAA";
    private static final String UPDATED_RANK = "BBBBB";
    private static final String DEFAULT_EXTRA = "AAAAA";
    private static final String UPDATED_EXTRA = "BBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_DOCDOC_ID = 1L;
    private static final Long UPDATED_DOCDOC_ID = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_UPDATED_STR = dateTimeFormatter.format(DEFAULT_LAST_UPDATED);

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private DoctorSearchRepository doctorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DoctorResource doctorResource = new DoctorResource();
        ReflectionTestUtils.setField(doctorResource, "doctorRepository", doctorRepository);
        ReflectionTestUtils.setField(doctorResource, "doctorSearchRepository", doctorSearchRepository);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        doctor = new Doctor();
        doctor.setFio(DEFAULT_FIO4);
        doctor.setAlias(DEFAULT_ALIAS);
        doctor.setRating(DEFAULT_RATING);
        doctor.setRatingInternal(DEFAULT_RATING_INTERNAL);
        doctor.setPriceFirst(DEFAULT_PRICE_FIRST);
        doctor.setPriceSpecial(DEFAULT_PRICE_SPECIAL);
        doctor.setSex(DEFAULT_SEX);
        doctor.setImg(DEFAULT_IMG);
        doctor.setReviewCount(DEFAULT_REVIEW_COUNT);
        doctor.setTextAbout(DEFAULT_TEXT_ABOUT);
        doctor.setExperiencaYear(DEFAULT_EXPERIENCA_YEAR);
        doctor.setDeparture(DEFAULT_DEPARTURE);
        doctor.setCategory(DEFAULT_CATEGORY);
        doctor.setDegree(DEFAULT_DEGREE);
        doctor.setRank(DEFAULT_RANK);
        doctor.setExtra(DEFAULT_EXTRA);
        doctor.setIsActive(DEFAULT_IS_ACTIVE);
        doctor.setDocdocId(DEFAULT_DOCDOC_ID);
        doctor.setLastUpdated(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor

        restDoctorMockMvc.perform(post("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getFio()).isEqualTo(DEFAULT_FIO4);
        assertThat(testDoctor.getAlias()).isEqualTo(DEFAULT_ALIAS);
        assertThat(testDoctor.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testDoctor.getRatingInternal()).isEqualTo(DEFAULT_RATING_INTERNAL);
        assertThat(testDoctor.getPriceFirst()).isEqualTo(DEFAULT_PRICE_FIRST);
        assertThat(testDoctor.getPriceSpecial()).isEqualTo(DEFAULT_PRICE_SPECIAL);
        assertThat(testDoctor.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testDoctor.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testDoctor.getReviewCount()).isEqualTo(DEFAULT_REVIEW_COUNT);
        assertThat(testDoctor.getTextAbout()).isEqualTo(DEFAULT_TEXT_ABOUT);
        assertThat(testDoctor.getExperiencaYear()).isEqualTo(DEFAULT_EXPERIENCA_YEAR);
        assertThat(testDoctor.getDeparture()).isEqualTo(DEFAULT_DEPARTURE);
        assertThat(testDoctor.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testDoctor.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testDoctor.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testDoctor.getExtra()).isEqualTo(DEFAULT_EXTRA);
        assertThat(testDoctor.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testDoctor.getDocdocId()).isEqualTo(DEFAULT_DOCDOC_ID);
        assertThat(testDoctor.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctors
        restDoctorMockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
                .andExpect(jsonPath("$.[*].fio").value(hasItem(DEFAULT_FIO4.toString())))
                .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
                .andExpect(jsonPath("$.[*].ratingInternal").value(hasItem(DEFAULT_RATING_INTERNAL.doubleValue())))
                .andExpect(jsonPath("$.[*].priceFirst").value(hasItem(DEFAULT_PRICE_FIRST)))
                .andExpect(jsonPath("$.[*].priceSpecial").value(hasItem(DEFAULT_PRICE_SPECIAL)))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
                .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG.toString())))
                .andExpect(jsonPath("$.[*].reviewCount").value(hasItem(DEFAULT_REVIEW_COUNT)))
                .andExpect(jsonPath("$.[*].textAbout").value(hasItem(DEFAULT_TEXT_ABOUT.toString())))
                .andExpect(jsonPath("$.[*].experiencaYear").value(hasItem(DEFAULT_EXPERIENCA_YEAR)))
                .andExpect(jsonPath("$.[*].departure").value(hasItem(DEFAULT_DEPARTURE.booleanValue())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE.toString())))
                .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.toString())))
                .andExpect(jsonPath("$.[*].extra").value(hasItem(DEFAULT_EXTRA.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].docdocId").value(hasItem(DEFAULT_DOCDOC_ID.intValue())))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.fio").value(DEFAULT_FIO4.toString()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.ratingInternal").value(DEFAULT_RATING_INTERNAL.doubleValue()))
            .andExpect(jsonPath("$.priceFirst").value(DEFAULT_PRICE_FIRST))
            .andExpect(jsonPath("$.priceSpecial").value(DEFAULT_PRICE_SPECIAL))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG.toString()))
            .andExpect(jsonPath("$.reviewCount").value(DEFAULT_REVIEW_COUNT))
            .andExpect(jsonPath("$.textAbout").value(DEFAULT_TEXT_ABOUT.toString()))
            .andExpect(jsonPath("$.experiencaYear").value(DEFAULT_EXPERIENCA_YEAR))
            .andExpect(jsonPath("$.departure").value(DEFAULT_DEPARTURE.booleanValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE.toString()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.toString()))
            .andExpect(jsonPath("$.extra").value(DEFAULT_EXTRA.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.docdocId").value(DEFAULT_DOCDOC_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

		int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        doctor.setFio(UPDATED_FIO4);
        doctor.setAlias(UPDATED_ALIAS);
        doctor.setRating(UPDATED_RATING);
        doctor.setRatingInternal(UPDATED_RATING_INTERNAL);
        doctor.setPriceFirst(UPDATED_PRICE_FIRST);
        doctor.setPriceSpecial(UPDATED_PRICE_SPECIAL);
        doctor.setSex(UPDATED_SEX);
        doctor.setImg(UPDATED_IMG);
        doctor.setReviewCount(UPDATED_REVIEW_COUNT);
        doctor.setTextAbout(UPDATED_TEXT_ABOUT);
        doctor.setExperiencaYear(UPDATED_EXPERIENCA_YEAR);
        doctor.setDeparture(UPDATED_DEPARTURE);
        doctor.setCategory(UPDATED_CATEGORY);
        doctor.setDegree(UPDATED_DEGREE);
        doctor.setRank(UPDATED_RANK);
        doctor.setExtra(UPDATED_EXTRA);
        doctor.setIsActive(UPDATED_IS_ACTIVE);
        doctor.setDocdocId(UPDATED_DOCDOC_ID);
        doctor.setLastUpdated(UPDATED_LAST_UPDATED);

        restDoctorMockMvc.perform(put("/api/doctors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doctor)))
                .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctors.get(doctors.size() - 1);
        assertThat(testDoctor.getFio()).isEqualTo(UPDATED_FIO4);
        assertThat(testDoctor.getAlias()).isEqualTo(UPDATED_ALIAS);
        assertThat(testDoctor.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testDoctor.getRatingInternal()).isEqualTo(UPDATED_RATING_INTERNAL);
        assertThat(testDoctor.getPriceFirst()).isEqualTo(UPDATED_PRICE_FIRST);
        assertThat(testDoctor.getPriceSpecial()).isEqualTo(UPDATED_PRICE_SPECIAL);
        assertThat(testDoctor.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testDoctor.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testDoctor.getReviewCount()).isEqualTo(UPDATED_REVIEW_COUNT);
        assertThat(testDoctor.getTextAbout()).isEqualTo(UPDATED_TEXT_ABOUT);
        assertThat(testDoctor.getExperiencaYear()).isEqualTo(UPDATED_EXPERIENCA_YEAR);
        assertThat(testDoctor.getDeparture()).isEqualTo(UPDATED_DEPARTURE);
        assertThat(testDoctor.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testDoctor.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testDoctor.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testDoctor.getExtra()).isEqualTo(UPDATED_EXTRA);
        assertThat(testDoctor.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testDoctor.getDocdocId()).isEqualTo(UPDATED_DOCDOC_ID);
        assertThat(testDoctor.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

		int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Get the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
