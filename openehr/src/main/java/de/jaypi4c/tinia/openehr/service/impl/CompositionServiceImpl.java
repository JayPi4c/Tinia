package de.jaypi4c.tinia.openehr.service.impl;

import de.jaypi4c.tinia.openehr.composition.CompositionFactory;
import de.jaypi4c.tinia.openehr.service.CompositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompositionServiceImpl implements CompositionService {

    private final CompositionFactory<?> compositionFactory;

    @Override
    public CompositionEntity createComposition(String[][] medicationMatrix, String date, String metadataJson) {
        return compositionFactory.createComposition(medicationMatrix, date, metadataJson);
    }

}
