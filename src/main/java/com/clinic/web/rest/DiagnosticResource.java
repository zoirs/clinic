package com.clinic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clinic.domain.Diagnostic;
import com.clinic.repository.DiagnosticRepository;
import com.clinic.repository.search.DiagnosticSearchRepository;
import com.clinic.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Diagnostic.
 */
@RestController
@RequestMapping("/api")
public class DiagnosticResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticResource.class);

    @Inject
    private DiagnosticRepository diagnosticRepository;

    @Inject
    private DiagnosticSearchRepository diagnosticSearchRepository;

    /**
     * POST  /diagnostics -> Create a new diagnostic.
     */
    @RequestMapping(value = "/diagnostics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagnostic> createDiagnostic(@Valid @RequestBody Diagnostic diagnostic) throws URISyntaxException {
        log.debug("REST request to save Diagnostic : {}", diagnostic);
        if (diagnostic.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new diagnostic cannot already have an ID").body(null);
        }
        Diagnostic result = diagnosticRepository.save(diagnostic);
        diagnosticSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/diagnostics/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("diagnostic", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /diagnostics -> Updates an existing diagnostic.
     */
    @RequestMapping(value = "/diagnostics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagnostic> updateDiagnostic(@Valid @RequestBody Diagnostic diagnostic) throws URISyntaxException {
        log.debug("REST request to update Diagnostic : {}", diagnostic);
        if (diagnostic.getId() == null) {
            return createDiagnostic(diagnostic);
        }
        Diagnostic result = diagnosticRepository.save(diagnostic);
        diagnosticSearchRepository.save(diagnostic);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("diagnostic", diagnostic.getId().toString()))
                .body(result);
    }

    /**
     * GET  /diagnostics -> get all the diagnostics.
     */
    @RequestMapping(value = "/diagnostics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Diagnostic> getAllDiagnostics() {
        log.debug("REST request to get all Diagnostics");
        return diagnosticRepository.findAll();
    }

    /**
     * GET  /diagnostics/:id -> get the "id" diagnostic.
     */
    @RequestMapping(value = "/diagnostics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diagnostic> getDiagnostic(@PathVariable Long id) {
        log.debug("REST request to get Diagnostic : {}", id);
        return Optional.ofNullable(diagnosticRepository.findOne(id))
            .map(diagnostic -> new ResponseEntity<>(
                diagnostic,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diagnostics/:id -> delete the "id" diagnostic.
     */
    @RequestMapping(value = "/diagnostics/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable Long id) {
        log.debug("REST request to delete Diagnostic : {}", id);
        diagnosticRepository.delete(id);
        diagnosticSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diagnostic", id.toString())).build();
    }

    /**
     * SEARCH  /_search/diagnostics/:query -> search for the diagnostic corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/diagnostics/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Diagnostic> searchDiagnostics(@PathVariable String query) {
        return StreamSupport
            .stream(diagnosticSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
