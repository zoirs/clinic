package com.clinic.sync.doctor;

import com.clinic.domain.*;
import com.clinic.repository.*;
import com.clinic.sync.SyncService;
import com.clinic.sync.metro.MetroParams;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SyncDoctor extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncDoctor.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/doctor/list/";    // start/0/count/5/city/1
    private final static String listName = "DoctorList";
    private final static String totalName = "Total";
    private ExecutorService service = Executors.newCachedThreadPool();
    int countAll = 0;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private MetroRepository metroRepository;
    @Autowired
    private SpecialityRepository specialityRepository;

    public void sync() {
        logger.info("Синхронизация докторов. Начало: " + DateTime.now());

        List<City> cities = cityRepository.findAll();
        for (City city : cities) {
            if (city.getName().equals("Москва")){
                syncForCity(city);
            }
        }

        logger.info("Синхронизация докторов. Конец: " + DateTime.now());
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
            + DoctorParams.startFrom.key + "/" + start + "/"
            + DoctorParams.countList.key + "/" + count + "/"
            + DoctorParams.cityID.key + "/" + city.getDocdocId());

        List<Map<String, Object>> doctorList = (List<Map<String, Object>>) map.get(listName);
        countAll = ((Double) map.get(totalName)).intValue();

        if (doctorList != null && !doctorList.isEmpty()) {
            for (final Map<String, Object> doctorMap : doctorList) {
                Future submit = service.submit(() -> migrate(doctorMap, city));
                try {
                    submit.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private void migrate(Map<String, Object> doctorMap, City city) {
        final Long docdocId = ((Double) doctorMap.get(DoctorParams.id.key)).longValue();
        final String name = (String) doctorMap.get(DoctorParams.name.key);
        final String alias = (String) doctorMap.get(DoctorParams.alias.key);
        final String rating = (String) doctorMap.get(DoctorParams.Rating.key);
        final String internalRating = (String) doctorMap.get(DoctorParams.InternalRating.key);
        final Integer sex = ((Double) doctorMap.get(DoctorParams.Sex.key)).intValue();
        final Integer reviewCount = ((Double) doctorMap.get(DoctorParams.ReviewCount.key)).intValue();
        final Integer departure = ((Double) doctorMap.get(DoctorParams.Departure.key)).intValue();


        final String img = (String) doctorMap.get(DoctorParams.Img.key);
        final Integer experienceYear = ((Double) doctorMap.get(DoctorParams.ExperienceYear.key)).intValue();
        final Integer price = ((Double) doctorMap.get(DoctorParams.Price.key)).intValue();
        final Integer specialPrice = ((Double) doctorMap.get(DoctorParams.SpecialPrice.key)).intValue();
        final String text = (String) doctorMap.get(DoctorParams.TextAbout.key);
        final String categiry = (String) doctorMap.get(DoctorParams.Category.key);
        final String degree = (String) doctorMap.get(DoctorParams.Degree.key);
        final String rank = (String) doctorMap.get(DoctorParams.Rank.key);
        final String extra = (String) doctorMap.get(DoctorParams.Extra.key);

        final List<Double> clinicIds = (List<Double>) doctorMap.get(DoctorParams.Clinics.key);   // Приходит id или массив исследований? нет в апи
        List<Map<String, Object>> metros = (List<Map<String, Object>>) doctorMap.get(DoctorParams.Metro.key);// Приходит id или массив исследований? нет в апи
        List<Map<String, Object>> specialities = (List<Map<String, Object>>) doctorMap.get(DoctorParams.Specialities.key);// Приходит id или массив исследований? нет в апи

// todo остальные поля

        Set<Clinic> clinics = Sets.newHashSet();
        if (clinicIds != null) {
            for (Double clinic : clinicIds) {
                Optional<Clinic> one = clinicRepository.findOneByDocdocId(clinic.longValue());
                if (one.isPresent()) {
                    clinics.add(one.get());
                }
            }
        }

        Set<Metro> doctorsMetros = Sets.newHashSet();
        if (metros != null) {
            for (Map<String, Object> metro : metros) {
                long metroId = Long.parseLong((String) metro.get(MetroParams.id.key));
                Optional<Metro> doctorsMetro = metroRepository.findOneByDocdocId(metroId);
                if (doctorsMetro.isPresent()) {
                    doctorsMetros.add(doctorsMetro.get());
                }
            }
        }

        Set<Speciality> doctorsSpecialities = Sets.newHashSet();
        if (specialities != null) {
            for (Map<String, Object> speciality : specialities) {
                long specialityId = Long.parseLong((String) speciality.get(MetroParams.id.key));
                Optional<Speciality> doctorsSpeciality = specialityRepository.findOneByDocdocId(specialityId);
                if (doctorsSpeciality.isPresent()) {
                    doctorsSpecialities.add(doctorsSpeciality.get());
                }
            }
        }

        Optional<Doctor> doctorOptional = doctorRepository.findOneByDocdocId(docdocId);

        Doctor doctor = doctorOptional.isPresent() ? doctorOptional.get() : new Doctor();

        doctor.setFio(name);
        doctor.setAlias(alias);
        doctor.setCity(city);
        doctor.setDocdocId(docdocId);
        doctor.setLastUpdated(DateTime.now());
        doctor.setRating(Float.parseFloat(rating));
        doctor.setRatingInternal(Float.parseFloat(internalRating));
        doctor.setPriceFirst(price);
        doctor.setPriceSpecial(specialPrice);
        doctor.setSex(sex);
        doctor.setImg(img);
        doctor.setReviewCount(reviewCount);
        doctor.setTextAbout(text);
        doctor.setExperiencaYear(experienceYear);
        doctor.setDeparture(departure == 1);
        doctor.setCategory(categiry);
        doctor.setDegree(degree);
        doctor.setRank(rank);
        doctor.setExtra(extra);
        doctor.setClinics(clinics);
        doctor.setMetros(doctorsMetros);
        doctor.setSpecialitys(doctorsSpecialities);

        doctorRepository.save(doctor);
    }
}
