package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Doctor;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.search.DoctorSearchRepository;
import com.clinic.web.rest.util.HeaderUtil;
import com.clinic.web.rest.util.PaginationUtil;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private DoctorSearchRepository doctorSearchRepository;

    /**
     * POST  /doctors -> Create a new doctor.
     */
    @RequestMapping(value = "/doctors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) throws URISyntaxException {
        log.debug("REST request to save Doctor : {}", doctor);
        if (doctor.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new doctor cannot already have an ID").body(null);
        }
        Doctor result = doctorRepository.save(doctor);
        doctorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/doctors/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("doctor", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /doctors -> Updates an existing doctor.
     */
    @RequestMapping(value = "/doctors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Doctor> updateDoctor(@Valid @RequestBody Doctor doctor) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}", doctor);
        if (doctor.getId() == null) {
            return createDoctor(doctor);
        }
        Doctor result = doctorRepository.save(doctor);
        doctorSearchRepository.save(doctor);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("doctor", doctor.getId().toString()))
                .body(result);
    }

    /**
     * GET  /doctors -> get all the doctors.
     */
    @RequestMapping(value = "/doctors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Doctor>> getAllDoctors(Pageable pageable)
        throws URISyntaxException {
        Page<Doctor> page = doctorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /doctors/:id -> get the "id" doctor.
     */
    @RequestMapping(value = "/doctors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        return Optional.ofNullable(doctorRepository.findOneWithEagerRelationships(id))
            .map(doctor -> new ResponseEntity<>(
                doctor,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /doctors/:id -> delete the "id" doctor.
     */
    @RequestMapping(value = "/doctors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete Doctor : {}", id);
        doctorRepository.delete(id);
        doctorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("doctor", id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctors/:query -> search for the doctor corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/doctors/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Doctor> searchDoctors(@PathVariable String query) {
        return null;
//        return StreamSupport
//            .stream(doctorSearchRepository.search(queryString(query)).spliterator(), false)
//            .collect(Collectors.toList());
    }
}
