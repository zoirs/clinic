package com.clinic.sync.area;

import com.clinic.domain.Area;
import com.clinic.domain.City;
import com.clinic.repository.AreaRepository;
import com.clinic.repository.CityRepository;
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
public class SyncArea extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncArea.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/district/city/";
    private final static String listName = "DistrictList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private AreaRepository areaRepository;

    public void sync() {
        logger.info("Синхронизация районов. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            syncForCity(city);
        }

        logger.info("Синхронизация районов. Конец: " + DateTime.now());
    }

    private void syncForCity(City city) {
        Map<String, Object> map = get(url + city.getDocdocId());
        List<Map<String, String>> areaList = (List<Map<String, String>>) map.get(listName);

        if (areaList != null && !areaList.isEmpty()) {
            for (final Map<String, String> cityMap : areaList) {
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
        final Long docdocId = Long.parseLong(streetMap.get(AreaParams.id.key));
        final String name = streetMap.get(AreaParams.name.key);
        final String alias = streetMap.get(AreaParams.alias.key);

        Optional<Area> streetOptional = areaRepository.findOneByDocdocId(docdocId);

        Area area = streetOptional.isPresent() ? streetOptional.get() : new Area();

        area.setName(name);
        area.setAlias(alias);
        area.setCity(city);
        area.setDocdocId(docdocId);
        area.setLastUpdated(DateTime.now());

        areaRepository.save(area);
    }


}
