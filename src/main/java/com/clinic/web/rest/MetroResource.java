package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Metro;
import com.clinic.repository.MetroRepository;
import com.clinic.repository.search.MetroSearchRepository;
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
 * REST controller for managing Metro.
 */
@RestController
@RequestMapping("/api")
public class MetroResource {

    private final Logger log = LoggerFactory.getLogger(MetroResource.class);

    @Inject
    private MetroRepository metroRepository;

    @Inject
    private MetroSearchRepository metroSearchRepository;

    /**
     * POST  /metros -> Create a new metro.
     */
    @RequestMapping(value = "/metros",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metro> createMetro(@Valid @RequestBody Metro metro) throws URISyntaxException {
        log.debug("REST request to save Metro : {}", metro);
        if (metro.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new metro cannot already have an ID").body(null);
        }
        Metro result = metroRepository.save(metro);
        metroSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/metros/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("metro", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /metros -> Updates an existing metro.
     */
    @RequestMapping(value = "/metros",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metro> updateMetro(@Valid @RequestBody Metro metro) throws URISyntaxException {
        log.debug("REST request to update Metro : {}", metro);
        if (metro.getId() == null) {
            return createMetro(metro);
        }
        Metro result = metroRepository.save(metro);
        metroSearchRepository.save(metro);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("metro", metro.getId().toString()))
                .body(result);
    }

    /**
     * GET  /metros -> get all the metros.
     */
    @RequestMapping(value = "/metros",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
    public List<Metro> getAllMetros() {
        log.debug("REST request to get all Metros");
        return metroRepository.findByCity_alias("msk");
    }

    /**
     * GET  /metros/:id -> get the "id" metro.
     */
    @RequestMapping(value = "/metros/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Metro> getMetro(@PathVariable Long id) {
        log.debug("REST request to get Metro : {}", id);
        return Optional.ofNullable(metroRepository.findOne(id))
            .map(metro -> new ResponseEntity<>(
                metro,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /metros/:id -> delete the "id" metro.
     */
    @RequestMapping(value = "/metros/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMetro(@PathVariable Long id) {
        log.debug("REST request to delete Metro : {}", id);
        metroRepository.delete(id);
        metroSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("metro", id.toString())).build();
    }

    /**
     * SEARCH  /_search/metros/:query -> search for the metro corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/metros/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Metro> searchMetros(@PathVariable String query) {
        return StreamSupport
            .stream(metroSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
