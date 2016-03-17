package com.clinic.web.rest;

import com.clinic.domain.Clinic;
import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import com.clinic.repository.ClinicRepository;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.MetroRepository;
import com.clinic.repository.search.DoctorSearchRepository;
import com.clinic.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.elasticsearch.index.query.QueryBuilders;
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
    private DoctorSearchRepository doctorSearchRepository;

    @Inject
    private MetroRepository metroRepository;

    /**
     * GET  /doctors -> get all the doctors.
     */
    @RequestMapping(value = "/find/doctors/{metro}/{speciality}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Doctor>> getAllDoctors(@PathVariable String metro,
                                                      @PathVariable String speciality,
                                                      Pageable pageable)
        throws URISyntaxException {
        System.out.println("metro = " + metro);
        System.out.println("speciality = " + speciality);
        System.out.println("pageable = " + pageable);
        Optional<Metro> m = metroRepository.findOneByAlias(metro);
        assert m.isPresent();                                  // TODO
//        Collection<Metro> metros = new ArrayList<>();
//        metros.add(m.get());
        Page<Doctor> page = doctorSearchRepository.findByMetros(pageable);
        System.out.println("page = " + page);
//        Page<Doctor> page2 = doctorSearchRepository.findByMetrosContaining(m.get(), pageable);
//        System.out.println("page2 = " + page2);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/find/doctors/" + metro + "/" + speciality);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /doctors -> get all the clinic.
     */
    @RequestMapping(value = "/find/clinics/{metro}/{speciality}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Clinic>> getAllClinics(@PathVariable String metro,
                                                      @PathVariable String speciality,
                                                      Pageable pageable)
        throws URISyntaxException {
        System.out.println("metro = " + metro);
        System.out.println("speciality = " + speciality);
        System.out.println("pageable = " + pageable);
        Page<Clinic> page = clinicRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clinics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
