package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Clinic;
import com.clinic.repository.ClinicRepository;
import com.clinic.repository.search.ClinicSearchRepository;
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
 * REST controller for managing Clinic.
 */
@RestController
@RequestMapping("/api")
public class ClinicResource {

    private final Logger log = LoggerFactory.getLogger(ClinicResource.class);

    @Inject
    private ClinicRepository clinicRepository;

    @Inject
    private ClinicSearchRepository clinicSearchRepository;

    /**
     * POST  /clinics -> Create a new clinic.
     */
    @RequestMapping(value = "/clinics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody Clinic clinic) throws URISyntaxException {
        log.debug("REST request to save Clinic : {}", clinic);
        if (clinic.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new clinic cannot already have an ID").body(null);
        }
        Clinic result = clinicRepository.save(clinic);
        clinicSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clinics/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("clinic", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /clinics -> Updates an existing clinic.
     */
    @RequestMapping(value = "/clinics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clinic> updateClinic(@Valid @RequestBody Clinic clinic) throws URISyntaxException {
        log.debug("REST request to update Clinic : {}", clinic);
        if (clinic.getId() == null) {
            return createClinic(clinic);
        }
        Clinic result = clinicRepository.save(clinic);
        clinicSearchRepository.save(clinic);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("clinic", clinic.getId().toString()))
                .body(result);
    }

    /**
     * GET  /clinics -> get all the clinics.
     */
    @RequestMapping(value = "/clinics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Clinic>> getAllClinics(Pageable pageable)
        throws URISyntaxException {
        Page<Clinic> page = clinicRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clinics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clinics/:id -> get the "id" clinic.
     */
    @RequestMapping(value = "/clinics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Clinic> getClinic(@PathVariable Long id) {
        log.debug("REST request to get Clinic : {}", id);
        return Optional.ofNullable(clinicRepository.findOneWithEagerRelationships(id))
            .map(clinic -> new ResponseEntity<>(
                clinic,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clinics/:id -> delete the "id" clinic.
     */
    @RequestMapping(value = "/clinics/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        log.debug("REST request to delete Clinic : {}", id);
        clinicRepository.delete(id);
        clinicSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clinic", id.toString())).build();
    }

    /**
     * SEARCH  /_search/clinics/:query -> search for the clinic corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/clinics/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Clinic> searchClinics(@PathVariable String query) {
        return StreamSupport
            .stream(clinicSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
