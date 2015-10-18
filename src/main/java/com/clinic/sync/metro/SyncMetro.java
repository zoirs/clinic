package com.clinic.sync.metro;

import com.clinic.domain.City;
import com.clinic.domain.Metro;
import com.clinic.repository.CityRepository;
import com.clinic.repository.MetroRepository;
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
public class SyncMetro extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncMetro.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/metro/city/";
    private final static String listName = "MetroList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private MetroRepository metroRepository;

    public void sync() {
        logger.info("Синхронизация метро. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            syncForCity(city);
        }

        logger.info("Синхронизация метро. Конец: " + DateTime.now());
    }

    private void syncForCity(City city) {
        Map<String, Object> map = get(url + city.getDocdocId());
        List<Map<String, String>> metroList = (List<Map<String, String>>) map.get(listName);

        if (metroList != null && !metroList.isEmpty()) {
            for (final Map<String, String> cityMap : metroList) {
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

    private void migrate(Map<String, String> metroMap, City city) {
        final Long docdocId = Long.parseLong(metroMap.get(MetroParams.id.key));
        final String name = metroMap.get(MetroParams.name.key);
        final String alias = metroMap.get(MetroParams.alias.key);
        final String lineName = metroMap.get(MetroParams.lineName.key);
        final String lineColor = metroMap.get(MetroParams.lineColor.key);
//        final String cityId = metroMap.get(MetroParams.cityId.key);

        Optional<Metro> metroOptional = metroRepository.findOneByDocdocId(docdocId);

        Metro metro = metroOptional.isPresent() ? metroOptional.get() : new Metro();

        metro.setName(name);
        metro.setAlias(alias);
        metro.setLineName(lineName);
        metro.setLineColor(lineColor);
        metro.setCity(city);
        metro.setDocdocId(docdocId);
        metro.setLastUpdated(DateTime.now());

        metroRepository.save(metro);
    }


}
