package com.clinic.sync.metro;

import com.clinic.domain.City;
import com.clinic.domain.Metro;
import com.clinic.repository.MetroRepository;
import com.clinic.sync.SyncService;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

@Service
public class SyncNearestMetro extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncNearestMetro.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/nearestStation/id/";
    private final static String listName = "StationList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private MetroRepository metroRepository;

    public void sync() {
        logger.info("Синхронизация ближайших метро. Начало: " + DateTime.now());


        List<Metro> metros = metroRepository.findAll();
        for (Metro metro : metros) {
            syncForMetro(metroRepository.findByAliasWithNearest(metro.getAlias()).get(0));
        }

        logger.info("Синхронизация ближайших метро. Конец: " + DateTime.now());
    }

    private void syncForMetro(Metro metro) {
        Map<String, Object> map = get(url + metro.getDocdocId());
        List<Map<String, String>> nearestMetros = (List<Map<String, String>>) map.get(listName);

        if (nearestMetros != null && !nearestMetros.isEmpty()) {
            Future submit = service.submit(() -> migrate(nearestMetros, metro));
            try {
                submit.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.toString());
                e.printStackTrace();
            }
        }
    }

    private void migrate(List<Map<String, String>> nearestMetros, Metro metro) {
        Set<Metro> nearest = new HashSet<Metro>();
        for (int i = 0; i < 5; i++) {
            if (nearestMetros.size() > i) {
                final Long docdocId = Long.parseLong(nearestMetros.get(i).get(MetroParams.id.key));
                Optional<Metro> m = metroRepository.findOneByDocdocId(docdocId);
                if (m.isPresent()){
                    nearest.add(m.get());
                }
            }

        }

        if (!isEmpty(nearest) && isEmpty(metro.getNearest())) {
            metro.setNearest(nearest);
            metroRepository.save(metro);
        }
    }


}
