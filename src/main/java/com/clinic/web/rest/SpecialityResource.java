package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Speciality;
import com.clinic.repository.SpecialityRepository;
import com.clinic.repository.search.SpecialitySearchRepository;
import com.clinic.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Speciality.
 */
@RestController
@RequestMapping("/api")
public class SpecialityResource {

    private final Logger log = LoggerFactory.getLogger(SpecialityResource.class);

    @Inject
    private SpecialityRepository specialityRepository;

    @Inject
    private SpecialitySearchRepository specialitySearchRepository;

    /**
     * POST  /specialitys -> Create a new speciality.
     */
    @RequestMapping(value = "/specialitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Speciality> createSpeciality(@Valid @RequestBody Speciality speciality) throws URISyntaxException {
        log.debug("REST request to save Speciality : {}", speciality);
        if (speciality.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new speciality cannot already have an ID").body(null);
        }
        Speciality result = specialityRepository.save(speciality);
        specialitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/specialitys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("speciality", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /specialitys -> Updates an existing speciality.
     */
    @RequestMapping(value = "/specialitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Speciality> updateSpeciality(@Valid @RequestBody Speciality speciality) throws URISyntaxException {
        log.debug("REST request to update Speciality : {}", speciality);
        if (speciality.getId() == null) {
            return createSpeciality(speciality);
        }
        Speciality result = specialityRepository.save(speciality);
        specialitySearchRepository.save(speciality);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("speciality", speciality.getId().toString()))
                .body(result);
    }

    /**
     * GET  /specialitys -> get all the specialitys.
     */
    @RequestMapping(value = "/specialitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
    public List<Speciality> getAllSpecialitys() {
        log.debug("REST request to get all Specialitys");
        return specialityRepository.findAllByOrderByNameAsc();
    }

    /**
     * GET  /specialitys/:id -> get the "id" speciality.
     */
    @RequestMapping(value = "/specialitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Speciality> getSpeciality(@PathVariable Long id) {
        log.debug("REST request to get Speciality : {}", id);
        return Optional.ofNullable(specialityRepository.findOne(id))
            .map(speciality -> new ResponseEntity<>(
                speciality,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /specialitys/:id -> delete the "id" speciality.
     */
    @RequestMapping(value = "/specialitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        log.debug("REST request to delete Speciality : {}", id);
        specialityRepository.delete(id);
        specialitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("speciality", id.toString())).build();
    }

    /**
     * SEARCH  /_search/specialitys/:query -> search for the speciality corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/specialitys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Speciality> searchSpecialitys(@PathVariable String query) {
        return StreamSupport
            .stream(specialitySearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
