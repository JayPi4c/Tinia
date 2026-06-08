package de.jaypi4c.tinia.openehr.util;

import de.jaypi4c.tinia.openehr.composition.CompositionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/// Initializer to ensure, the required compositions are present in the openEHR-Repo.
/// Will retry until a connection to the openEHR-Repo could be established.
@Slf4j
@Component
@RequiredArgsConstructor
public class EhrInitializer {

    private final OpenEhrClient ehrClient;
    private final CompositionFactory<?> compositionFactory;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> retryTask;

    @PostConstruct
    public void startRetrying() {
        retryTask = scheduler.scheduleWithFixedDelay(
                this::tryInitialize,
                0,
                15,
                TimeUnit.SECONDS
        );
    }

    private void tryInitialize() {
        try {
            ehrClient.templateEndpoint()
                    .ensureExistence(compositionFactory.getTemplateId());

            log.info("Successfully connected to EHRBase");

            retryTask.cancel(false); // stop future executions
            scheduler.shutdown();
        } catch (Exception e) {
            log.warn("EHRBase not available yet");
        }
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
