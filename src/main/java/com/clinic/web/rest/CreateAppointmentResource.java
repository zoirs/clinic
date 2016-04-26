package com.clinic.web.rest;

import com.clinic.domain.Apointmen;
import com.clinic.domain.Clinic;
import com.clinic.domain.Doctor;
import com.clinic.domain.Metro;
import com.clinic.domain.Speciality;
import com.clinic.repository.ClinicRepository;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.MetroRepository;
import com.clinic.repository.SpecialityRepository;
import com.clinic.sync.SyncService;
import com.clinic.web.rest.util.HeaderUtil;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class CreateAppointmentResource {

    private final Logger log = LoggerFactory.getLogger(CreateAppointmentResource.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.4/json/request";

    @RequestMapping(value = "/createappointmen",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Apointmen apointmen) throws URISyntaxException {

        log.debug(apointmen.toString());

        Map<String, String> param = new HashMap<String, String>();

        Map<String, Object> post = SyncService.post(url, param);
        String t = "{euccess:true, message:'sa ds'}";
        System.out.println("post = " + post);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appointmen", "1")).build();
//        return new ResponseEntity<>(success, HeaderUtil.createAlert(success, success), HttpStatus.OK);
    }

}
