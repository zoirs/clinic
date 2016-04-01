package com.clinic.web.rest;

import com.clinic.domain.Clinic;
import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import com.clinic.domain.Speciality;
import com.clinic.repository.ClinicRepository;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.MetroRepository;
import com.clinic.repository.SpecialityRepository;
import com.clinic.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class FindResource {

    private final Logger log = LoggerFactory.getLogger(FindResource.class);

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private ClinicRepository clinicRepository;

    @Inject
    private MetroRepository metroRepository;

    @Inject
    private SpecialityRepository specialityRepository;

    @RequestMapping(value = "/find/doctors/{metro}/{speciality}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Doctor>> getAllDoctors(@PathVariable String metro, @PathVariable String speciality, Pageable pageable) throws URISyntaxException {
        Collection<Metro> metros = getMetros(metro);
        Collection<Speciality> specialities = getSpecialities(speciality);

        Page<Doctor> page;
        if (metros.isEmpty() && specialities.isEmpty()) {
            page = doctorRepository.findAll(pageable);
        } else if (metros.isEmpty()) {
            page = doctorRepository.findBySpecialities(specialities, pageable);
        } else if (specialities.isEmpty()) {
            page = doctorRepository.findByMetros(metros, pageable);
        } else {
            page = doctorRepository.findByMetrosAndSpecialities(metros, specialities, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/find/doctors/" + metro + "/" + speciality);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/find/clinics/{metro}/{speciality}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Clinic>> getAllClinics(@PathVariable String metro, @PathVariable String speciality, Pageable pageable) throws URISyntaxException {
        Collection<Metro> metros = getMetros(metro);
        Collection<Speciality> specialities = getSpecialities(speciality);

        Page<Clinic> page;
        if (metros.isEmpty() && specialities.isEmpty()) {
            page = clinicRepository.findAll(pageable);
        } else if (metros.isEmpty()) {
            page = clinicRepository.findBySpecialities(specialities, pageable);
        } else if (specialities.isEmpty()) {
            page = clinicRepository.findByMetros(metros, pageable);
        } else {
            page = clinicRepository.findByMetrosAndSpecialities(metros, specialities, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/find/clinics/" + metro + "/" + speciality);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private Collection<Speciality> getSpecialities(String speciality) {
        Optional<Speciality> s = specialityRepository.findOneByAlias(speciality);
        if (!s.isPresent()) {
            return Collections.EMPTY_LIST;
        }
        Collection<Speciality> specialities = new ArrayList<>();
        specialities.add(s.get());
        return specialities;
    }

    private Collection<Metro> getMetros(String metro) {
        List<Metro> metros = metroRepository.findByAliasWithNearest(metro);
        if (metros.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return metros.get(0).getNearest();
    }

}
