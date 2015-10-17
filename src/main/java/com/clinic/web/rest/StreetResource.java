package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Street;
import com.clinic.repository.StreetRepository;
import com.clinic.repository.search.StreetSearchRepository;
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
 * REST controller for managing Street.
 */
@RestController
@RequestMapping("/api")
public class StreetResource {

    private final Logger log = LoggerFactory.getLogger(StreetResource.class);

    @Inject
    private StreetRepository streetRepository;

    @Inject
    private StreetSearchRepository streetSearchRepository;

    /**
     * POST  /streets -> Create a new street.
     */
    @RequestMapping(value = "/streets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Street> createStreet(@Valid @RequestBody Street street) throws URISyntaxException {
        log.debug("REST request to save Street : {}", street);
        if (street.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new street cannot already have an ID").body(null);
        }
        Street result = streetRepository.save(street);
        streetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/streets/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("street", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /streets -> Updates an existing street.
     */
    @RequestMapping(value = "/streets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Street> updateStreet(@Valid @RequestBody Street street) throws URISyntaxException {
        log.debug("REST request to update Street : {}", street);
        if (street.getId() == null) {
            return createStreet(street);
        }
        Street result = streetRepository.save(street);
        streetSearchRepository.save(street);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("street", street.getId().toString()))
                .body(result);
    }

    /**
     * GET  /streets -> get all the streets.
     */
    @RequestMapping(value = "/streets",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Street>> getAllStreets(Pageable pageable)
        throws URISyntaxException {
        Page<Street> page = streetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/streets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /streets/:id -> get the "id" street.
     */
    @RequestMapping(value = "/streets/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Street> getStreet(@PathVariable Long id) {
        log.debug("REST request to get Street : {}", id);
        return Optional.ofNullable(streetRepository.findOne(id))
            .map(street -> new ResponseEntity<>(
                street,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /streets/:id -> delete the "id" street.
     */
    @RequestMapping(value = "/streets/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStreet(@PathVariable Long id) {
        log.debug("REST request to delete Street : {}", id);
        streetRepository.delete(id);
        streetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("street", id.toString())).build();
    }

    /**
     * SEARCH  /_search/streets/:query -> search for the street corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/streets/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Street> searchStreets(@PathVariable String query) {
        return StreamSupport
            .stream(streetSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
