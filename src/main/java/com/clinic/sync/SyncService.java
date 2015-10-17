package com.clinic.sync;

import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract public class SyncService {

    static final private int countOfAttempts = 4;
    private final Logger logger = LoggerFactory.getLogger(SyncService.class);

    public Map<String, Object> get(String url) {
        Map<String, Object> result = new HashMap();

        int currentAttemp = 1;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String lp = "partner.1755:hZOpJ807";  // TODO [chernyshev.dl] логин и пароль вынести в конфиг приложения
        headers.set("Authorization", "Basic "+ Base64.encodeBase64String(lp.getBytes()));
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = null;

        while (currentAttemp < countOfAttempts) {
            try {
                response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                if (response.getStatusCode().value() == 200) {
                    break;
                }
                logger.error("Ошибка доступа к " + url + ". Статус " + response.getStatusCode().value());

            } catch (Exception e) {
                logger.error("Ошибка доступа к " + url + ". " + e.toString());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ignore) {
                }
            }
            currentAttemp++;
        }
        if (response != null && response.getStatusCode().value() == 200) {
            System.out.println("response = " + response.getBody());
            result = new Gson().fromJson(response.getBody(), HashMap.class);
        }
        return result;
    }
}
