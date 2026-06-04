package de.jaypi4c.tinia.openehr.service.impl;

import de.jaypi4c.tinia.openehr.service.CompositionService;
import de.jaypi4c.tinia.openehr.service.NetworkService;
import de.jaypi4c.tinia.openehr.service.OpenEhrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenEhrServiceImpl implements OpenEhrService {

    private final CompositionService compositionService;
    private final NetworkService networkService;

    @Override
    public CompositionEntity process(String[][] medicationMatrix, String date, String metadataJson) {
        CompositionEntity composition = compositionService.createComposition(medicationMatrix, date, metadataJson);
        composition = networkService.sendData(composition);
        return composition;
    }
}
