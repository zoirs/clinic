package com.clinic.sync.street;

import com.clinic.domain.City;
import com.clinic.domain.Street;
import com.clinic.repository.CityRepository;
import com.clinic.repository.StreetRepository;
import com.clinic.sync.SyncService;
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

@Service
public class SyncStreet extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncStreet.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/street/city/";
    private final static String listName = "StreetList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StreetRepository streetRepository;

    public void sync() {
        logger.info("Синхронизация улиц. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            syncForCity(city);
        }

        logger.info("Синхронизация улиц. Конец: " + DateTime.now());
    }

    private void syncForCity(City city) {
        Map<String, Object> map = get(url + city.getDocdocId());
        List<Map<String, String>> streetList = (List<Map<String, String>>) map.get(listName);

        if (streetList != null && !streetList.isEmpty()) {
            for (final Map<String, String> cityMap : streetList) {
                Future submit = service.submit(() -> migrate(cityMap, city));
                try {
                    submit.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }


    private void migrate(Map<String, String> streetMap, City city) {
        final Long docdocId = Long.parseLong(streetMap.get(StreetParams.id.key));
        final String name = streetMap.get(StreetParams.name.key);
        final String alias = streetMap.get(StreetParams.alias.key);

        Optional<Street> streetOptional = streetRepository.findOneByDocdocId(docdocId);

        Street street = streetOptional.isPresent() ? streetOptional.get() : new Street();

        street.setName(name);
        street.setAlias(alias);
        street.setCity(city);
        street.setDocdocId(docdocId);
        street.setLastUpdate(DateTime.now());

        streetRepository.save(street);
    }


}
