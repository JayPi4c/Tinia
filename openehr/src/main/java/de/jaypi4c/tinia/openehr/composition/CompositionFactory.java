package de.jaypi4c.tinia.openehr.composition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.archetyped.FeederAuditDetails;
import com.nedap.archie.rm.datavalues.encapsulated.DvEncapsulated;
import com.nedap.archie.rm.datavalues.encapsulated.DvParsable;
import com.nedap.archie.rm.generic.PartyIdentified;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CompositionFactory<T extends CompositionEntity> {

    /**
     * Creates a composition with the given parameters.
     *
     * @param args     2D String array, the first row is the header of the data table
     * @param date     the date when the medication plan was printed
     * @param metadata the metadata of the pdf file
     * @return Composition with the given parameters
     */
    T createComposition(String[][] args, String date, String metadata);

    String getTemplateId();

    /**
     * Prepare composition with default values that are mandatory but always the same.
     *
     * @param composition to prepare
     * @return prepared composition
     */
    default T prepareComposition(T composition) {
        composition.setLanguage(Language.DE);
        PartyIdentified composer = new PartyIdentified();
        composer.setName("Tinia (automated)");
        composition.setComposer(composer);
        composition.setTerritory(Territory.DE);

        composition.setStartTimeValue(LocalDateTime.now());
        composition.setEndTimeValue(LocalDateTime.now());
        composition.setSettingDefiningCode(Setting.HOME);

        // TODO Hardcoded Krankenhaus?
        PartyIdentified healthCareFacility = new PartyIdentified();
        healthCareFacility.setName("Krankenhaus");
        composition.setHealthCareFacility(healthCareFacility);

        return composition;

    }

    /**
     * Create FeederAudit object with metadata.
     *
     * @param metadataJson json string with pdf metadata
     * @return FeederAudit object
     * @see <a href="https://specifications.openehr.org/releases/RM/latest/data_types.html#_examples">OpenEHR Examples</a>
     */
    default FeederAudit createFeederAudit(String metadataJson) {
        FeederAudit feederAudit = new FeederAudit();
        FeederAuditDetails feederAuditDetails = new FeederAuditDetails();
        feederAuditDetails.setSystemId("Tinia (automated)");
        feederAudit.setOriginatingSystemAudit(feederAuditDetails);
        DvEncapsulated originalContent = new DvParsable(metadataJson, "json");
        feederAudit.setOriginalContent(originalContent);

        return feederAudit;
    }

    default String getCaseID() {
        return UUID.randomUUID().toString();
    }
}
