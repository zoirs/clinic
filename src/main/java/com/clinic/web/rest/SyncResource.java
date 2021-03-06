package com.clinic.web.rest;

import com.clinic.sync.area.SyncArea;
import com.clinic.sync.city.SyncCity;
import com.clinic.sync.clinic.SyncClinic;
import com.clinic.sync.diagnostic.SyncDiagnostic;
import com.clinic.sync.doctor.SyncDoctor;
import com.clinic.sync.metro.SyncMetro;
import com.clinic.sync.speciality.SyncSpeciality;
import com.clinic.sync.street.SyncStreet;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Sync.
 */
@RestController
@RequestMapping("/api")
public class SyncResource {

    private final Logger log = LoggerFactory.getLogger(SyncResource.class);

    @Autowired
    SyncCity syncCity;
    @Autowired
    SyncMetro syncMetro;
    @Autowired
    SyncStreet syncStreet;
    @Autowired
    SyncSpeciality syncSpeciality;
    @Autowired
    SyncArea syncArea;
    @Autowired
    SyncDiagnostic syncDiagnostic;
    @Autowired
    SyncClinic syncClinic;
    @Autowired
    SyncDoctor syncDoctor;

    @RequestMapping(value = "/sync/{data}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> sync(@PathVariable String data) {
        log.debug("REST request to sync ", data);
        switch (data){
            case "city":
                syncCity.sync();
                break;
            case "metro":
                syncMetro.sync();
                break;
            case "street":
                syncStreet.sync();
                break;
            case "speciality":
                syncSpeciality.sync();
                break;
            case "area":
                syncArea.sync();
                break;
            case "diagnostic":
                syncDiagnostic.sync();
                break;
            case "clinic":
                syncClinic.sync();
                break;
            case "doctor":
                syncDoctor.sync();
                break;
            case "all": {
                syncCity.sync();
                syncMetro.sync();
                syncStreet.sync();
                syncSpeciality.sync();
                syncArea.sync();
                syncDiagnostic.sync();
                syncClinic.sync();
                syncDoctor.sync();
                break;
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
