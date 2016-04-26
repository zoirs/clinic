package com.clinic.sync.clinic;

import com.clinic.domain.*;
import com.clinic.repository.*;
import com.clinic.sync.SyncService;
import com.clinic.sync.diagnostic.DiagnosticParams;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SyncClinic extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncClinic.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/clinic/list/";    // start/0/count/5/city/1
    private final static String listName = "ClinicList";
    private ExecutorService service = Executors.newCachedThreadPool();
    int countAll = 0;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private MetroRepository metroRepository;
    @Autowired
    private StreetRepository streetRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private DiagnosticRepository diagnosticRepository;
    @Autowired
    private SpecialityRepository specialityRepository;

    public void sync() {
        logger.info("Синхронизация клиник. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            syncForCity(city);
        }

        logger.info("Синхронизация клиник. Конец: " + DateTime.now());
    }

    private void syncForCity(City city) {
        int start = 0;
        int count = 500;

        while (start == 0 || start < countAll) {

            if ((start != 0) && ((start + count) > countAll)) {
                count = countAll - start;
            }

            logger.info("город: " + city.getName() + ", c " + start + " " + count + "шт. " + DateTime.now());

            getPart(city, start, count);
            start += count;
        }
    }

    private void getPart(City city, int start, int count) {
        Map<String, Object> map = get(url
            + ClinicParams.startFrom.key + "/" + start + "/"
            + ClinicParams.countList.key + "/" + count + "/"
            + ClinicParams.cityID.key + "/" + city.getDocdocId());

        List<Map<String, Object>> clinicList = (List<Map<String, Object>>) map.get(listName);

        if (clinicList != null && !clinicList.isEmpty()) {
            for (final Map<String, Object> clinicMap : clinicList) {
                Future submit = service.submit(() -> migrate(clinicMap, city));
                try {
                    submit.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private void migrate(Map<String, Object> clinicMap, City city) {
        final Long docdocId = ((Double) clinicMap.get(ClinicParams.id.key)).longValue();
        final String name = (String) clinicMap.get(ClinicParams.name.key);
//        final String alias = (String) clinicMap.get(ClinicParams.alias.key);
        final String rewriteName = (String) clinicMap.get(ClinicParams.RewriteName.key);
        final String url = (String) clinicMap.get(ClinicParams.URL.key);
        final String longitude = (String) clinicMap.get(ClinicParams.Longitude.key);
        final String latitude = (String) clinicMap.get(ClinicParams.Latitude.key);
        final String streetName = (String) clinicMap.get(ClinicParams.Street.key);
        final String streetId = (String) clinicMap.get(ClinicParams.StreetId.key);
        final String house = (String) clinicMap.get(ClinicParams.House.key);
        final String description = (String) clinicMap.get(ClinicParams.Description.key);
        final String shortDescription = (String) clinicMap.get(ClinicParams.ShortDescription.key);
        final String isDiagnostic = (String) clinicMap.get(ClinicParams.IsDiagnostic.key);
        final String isClinic = (String) clinicMap.get(ClinicParams.IsClinic.key);
        final String isDoctor = (String) clinicMap.get(ClinicParams.IsDoctor.key);
        final String phone = (String) clinicMap.get(ClinicParams.Phone.key);
        final String phoneAppointment = (String) clinicMap.get(ClinicParams.PhoneAppointment.key);
        final String replacementPhone = (String) clinicMap.get(ClinicParams.ReplacementPhone.key);
        final String scheduleState = (String) clinicMap.get(ClinicParams.ScheduleState.key);
        final String districtId = (String) clinicMap.get(ClinicParams.DistrictId.key);
        final String email = (String) clinicMap.get(ClinicParams.Email.key);
        final String minPrice = (String) clinicMap.get(ClinicParams.MinPrice.key);
        final String maxPrice = (String) clinicMap.get(ClinicParams.MaxPrice.key);
        final List<Map<String, String>> diagnosticIds = (List<Map<String, String>>) clinicMap.get(ClinicParams.Diagnostics.key);   // Приходит id или массив исследований? нет в апи
        final List<Map<String, String>> metroStantions = (List<Map<String, String>>) clinicMap.get(ClinicParams.Stations.key);
        final List<Map<String, String>> specialitiIds = (List<Map<String, String>>) clinicMap.get(ClinicParams.Specialities.key);
        // todo остальные поля

        Street street = null;
        if (StringUtils.isNotEmpty(streetId)) {
            Optional<Street> streetOptional = streetRepository.findOneByDocdocId(Long.parseLong(streetId));
            if (streetOptional.isPresent()) {
                street = streetOptional.get();
            }
        }

        Area area = null;
        if (StringUtils.isNotEmpty(districtId)) {
            Optional<Area> areaOptional = areaRepository.findOneByDocdocId(Long.parseLong(streetId));
            if (areaOptional.isPresent()) {
                area = areaOptional.get();
            }
        }

        Set<Diagnostic> diagnostics = Sets.newHashSet();
        if (diagnosticIds != null) {
            for (Map<String, String> diagnostic : diagnosticIds) {
                long diagnosticDocdocId = Long.parseLong(diagnostic.get(DiagnosticParams.id.key));
                Optional<Diagnostic> one = diagnosticRepository.findOneByDocdocId(diagnosticDocdocId);
                if (one.isPresent()) {
                    diagnostics.add(one.get());
                }
            }
        }


        Set<Metro> stations = Sets.newHashSet();
        for (Map<String, String> metroStation : metroStantions) {
            long metroStationDocdocId = Long.parseLong(metroStation.get(DiagnosticParams.id.key));
            Optional<Metro> oneByDocdocId = metroRepository.findOneByDocdocId(metroStationDocdocId);
            if (oneByDocdocId.isPresent()) {
                stations.add(oneByDocdocId.get());
            }
        }

        Set<Speciality> specialities = Sets.newHashSet();
        for (Map<String, String> speciality : specialitiIds) {
            long specialitiDocdocId = Long.parseLong(speciality.get(DiagnosticParams.id.key));
            Optional<Speciality> one = specialityRepository.findOneByDocdocId(specialitiDocdocId);
            if (one.isPresent()) {
                specialities.add(one.get());
            }
        }

        Optional<Clinic> clinicOptional = clinicRepository.findOneByDocdocId(docdocId);

        Clinic clinic = clinicOptional.isPresent() ? clinicOptional.get() : new Clinic();

        clinic.setName(name);
        clinic.setAlias("delete field");
        clinic.setCity(city);
        clinic.setDocdocId(docdocId);
        clinic.setLastUpdated(ZonedDateTime.now());
        clinic.setShortName(rewriteName);
        clinic.setUrl(url);
        clinic.setLongitude(Float.parseFloat(longitude));
        clinic.setLatitude(Float.parseFloat(latitude));
        clinic.setStreet(street);
        clinic.setHouse(house);
        clinic.setDescription(description);
        clinic.setShortDescription(shortDescription);
        clinic.setIsDiagnostic("yes".equals(isDiagnostic));
        clinic.setIsClinic("yes".equals(isClinic));
        clinic.setIsDoctor("yes".equals(isDoctor));
        clinic.setPhoneContact(phone);
        clinic.setPhoneAppointmen(phoneAppointment);
        clinic.setPhoneReplacement(replacementPhone);
        clinic.setScheduleStateOnline("yes".equals(scheduleState));
        clinic.setArea(area);
        clinic.setEmail(email);
        if (minPrice != null) {
            clinic.setMinPrice(new BigDecimal(minPrice));
        }
        if (maxPrice != null) {
            clinic.setMaxPrice(new BigDecimal(maxPrice));
        }
//        clinic.setDiagnostics(diagnostics);
        clinic.setMetros(stations);
        clinic.setSpecialitys(specialities);
        clinic.setDiagnostics(diagnostics);

        clinicRepository.save(clinic);
    }


}
