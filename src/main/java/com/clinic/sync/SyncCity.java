package com.clinic.sync;

import com.clinic.domain.City;
import com.clinic.repository.CityRepository;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class SyncCity extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncCity.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/city/";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private CityRepository cityRepository;

    public void sync() {
        logger.info("Синхронизация городов. Начало: " + DateTime.now());

        Map<String, Object> map = get(url);
        List<Map<String, String>> cityList = (List<Map<String, String>>) map.get("CityList");

        for (final Map<String, String> cityMap : cityList) {
            Future submit = service.submit(() -> migrate(cityMap));
            try {
                submit.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.toString());
                e.printStackTrace();
            }
        }
        logger.info("Синхронизация городов. Конец: " + DateTime.now());
    }

    public void migrate(Map<String, String> cityMap) {

        final String name = cityMap.get(CityParams.name.key);
        final String alias = cityMap.get(CityParams.alias.key);
        final String latitude = cityMap.get(CityParams.Latitude.key);
        final String longitude = cityMap.get(CityParams.Longitude.key);
        final Long docdocId = Long.parseLong(cityMap.get(CityParams.id.key));

        Optional<City> cityOptional = cityRepository.findOneByDocdocId(docdocId);

        City city = cityOptional.isPresent() ? cityOptional.get() : new City();

        city.setName(name);
        city.setAlias(alias);
        city.setDocdocId(docdocId);
        city.setLastUpdated(DateTime.now());
        city.setLatitude(isNotEmpty(latitude) ? Float.parseFloat(latitude) : null);
        city.setLongitude(isNotEmpty(longitude) ? Float.parseFloat(longitude) : null);

        cityRepository.save(city);

    }

}
