package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Area;
import com.clinic.repository.AreaRepository;
import com.clinic.repository.search.AreaSearchRepository;
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
 * REST controller for managing Area.
 */
@RestController
@RequestMapping("/api")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    @Inject
    private AreaRepository areaRepository;

    @Inject
    private AreaSearchRepository areaSearchRepository;

    /**
     * POST  /areas -> Create a new area.
     */
    @RequestMapping(value = "/areas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Area> createArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to save Area : {}", area);
        if (area.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new area cannot already have an ID").body(null);
        }
        Area result = areaRepository.save(area);
        areaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/areas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("area", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /areas -> Updates an existing area.
     */
    @RequestMapping(value = "/areas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Area> updateArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to update Area : {}", area);
        if (area.getId() == null) {
            return createArea(area);
        }
        Area result = areaRepository.save(area);
        areaSearchRepository.save(area);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("area", area.getId().toString()))
                .body(result);
    }

    /**
     * GET  /areas -> get all the areas.
     */
    @RequestMapping(value = "/areas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Area>> getAllAreas(Pageable pageable)
        throws URISyntaxException {
        Page<Area> page = areaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/areas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /areas/:id -> get the "id" area.
     */
    @RequestMapping(value = "/areas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Area> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);
        return Optional.ofNullable(areaRepository.findOne(id))
            .map(area -> new ResponseEntity<>(
                area,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /areas/:id -> delete the "id" area.
     */
    @RequestMapping(value = "/areas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);
        areaRepository.delete(id);
        areaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("area", id.toString())).build();
    }

    /**
     * SEARCH  /_search/areas/:query -> search for the area corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/areas/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Area> searchAreas(@PathVariable String query) {
        return StreamSupport
            .stream(areaSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
