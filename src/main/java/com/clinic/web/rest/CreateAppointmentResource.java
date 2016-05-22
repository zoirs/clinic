package com.clinic.web.rest;

import com.clinic.domain.Apointmen;
import com.clinic.sync.SyncService;
import com.clinic.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
        log.debug(apointmen.toParams().toString());

        Map<String, String> param = new HashMap<String, String>();

        Map<String, Object> post = SyncService.post(url, param);
        String t = "{euccess:true, message:'sa ds'}";
        System.out.println("post = " + post);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appointmen", "1")).build();
    }

}
