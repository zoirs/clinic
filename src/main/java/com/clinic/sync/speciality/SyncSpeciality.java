package com.clinic.sync.speciality;

import com.clinic.domain.City;
import com.clinic.domain.Speciality;
import com.clinic.repository.CityRepository;
import com.clinic.repository.SpecialityRepository;
import com.clinic.sync.SyncService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SyncSpeciality extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncSpeciality.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/speciality/city/";
    private final static String listName = "SpecList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private SpecialityRepository specialityRepository;

    public void sync() {
        logger.info("Синхронизация специальностей. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            syncForCity(city);
        }

        logger.info("Синхронизация специальностей. Конец: " + DateTime.now());
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


    private void migrate(Map<String, String> specialityMap, City city) {
        final Long docdocId = Long.parseLong(specialityMap.get(SpecialityParams.id.key));
        final String name = specialityMap.get(SpecialityParams.name.key);
        final String alias = specialityMap.get(SpecialityParams.alias.key);
        final String nameGenitive = specialityMap.get(SpecialityParams.NameGenitive.key);
        final String namePlural = specialityMap.get(SpecialityParams.NamePlural.key);
        final String namePluralGenitive = specialityMap.get(SpecialityParams.NamePluralGenitive.key);

        Optional<Speciality> streetOptional = specialityRepository.findOneByDocdocId(docdocId);

        Speciality speciality = streetOptional.isPresent() ? streetOptional.get() : new Speciality();

        speciality.setName(name);
        speciality.setAlias(alias);
        speciality.setNamePlural(namePlural);
        speciality.setNameGenitive(nameGenitive);
        speciality.setNamePluralGenitive(namePluralGenitive);
        speciality.setDocdocId(docdocId);
        speciality.setLastUpdated(ZonedDateTime.now());

        specialityRepository.save(speciality);
    }


}
