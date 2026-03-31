package de.jaypi4c.tinia.openehr.service;

import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;

public interface OpenEhrService {

    /// Processes the given medication matrix and metadata to create a composition entity, which will automatically be uploaded to an ehr Server.
    ///
    /// @param medicationMatrix the medication matrix to process
    /// @param date             the date of the medication
    /// @param metadataJson     the metadata as JSON string
    /// @return the created composition entity
    CompositionEntity process(String[][] medicationMatrix, String date, String metadataJson);

}
