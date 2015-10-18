package com.clinic.sync.diagnostic;

import com.clinic.domain.Diagnostic;
import com.clinic.repository.DiagnosticRepository;
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
public class SyncDiagnostic extends SyncService {

    private final static Logger logger = LoggerFactory.getLogger(SyncDiagnostic.class);
    private final static String url = "https://back.docdoc.ru/api/rest/1.0.5/json/diagnostic/";
    private final static String listName = "DiagnosticList";
    private ExecutorService service = Executors.newCachedThreadPool();

    @Autowired
    private DiagnosticRepository diagnosticRepository;

    public void sync() {
        logger.info("Синхронизация услуг. Начало: " + DateTime.now());

        Map<String, Object> map = get(url);
        List<Map<String, Object>> diagnostics = (List<Map<String, Object>>) map.get(listName);

        syncDiagnostic(diagnostics, null);

        logger.info("Синхронизация районов. Конец: " + DateTime.now());
    }

    private void syncDiagnostic(List<Map<String, Object>> diagnostics, final Diagnostic parent) {

        if (diagnostics != null && !diagnostics.isEmpty()) {
            for (final Map<String, Object> diagnosticMap : diagnostics) {
                Future submit = service.submit(() -> migrate(diagnosticMap, parent));
                try {
                    List<Map<String, Object>> diagnosticSubList = (List<Map<String, Object>>) diagnosticMap.get(DiagnosticParams.subDiagnosticList.key);
                    if (diagnosticSubList != null && !diagnosticSubList.isEmpty()) {
                        syncDiagnostic(diagnosticSubList, (Diagnostic) submit.get());
                    } else {
                        submit.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private Diagnostic migrate(Map<String, Object> streetMap, Diagnostic parent) {
        final Long docdocId = Long.parseLong((String) streetMap.get(DiagnosticParams.id.key));
        final String name = (String) streetMap.get(DiagnosticParams.name.key);
        final String alias = (String) streetMap.get(DiagnosticParams.alias.key);

        Optional<Diagnostic> streetOptional = diagnosticRepository.findOneByDocdocId(docdocId);

        Diagnostic diagnostic = streetOptional.isPresent() ? streetOptional.get() : new Diagnostic();

        diagnostic.setName(name);
        diagnostic.setAlias(alias);
        diagnostic.setDiagnostic(parent);
        diagnostic.setDocdocId(docdocId);
        diagnostic.setLastUpdated(DateTime.now());

        return diagnosticRepository.save(diagnostic);
    }

}
