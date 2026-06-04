package de.jaypi4c.tinia.openehr.service;

import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;

public interface NetworkService {

    CompositionEntity sendData(CompositionEntity composition);

}
