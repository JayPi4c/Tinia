package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.848542850+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class TaeglicheDosierungBestimmtesEreignisCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bestimmtes Ereignis/Ereignis
     * Description: Der Name des Ereignisses, das die Aktivität auslöst.
     * Comment: Zum Beispiel: "Vor jeder Mahlzeit", "vor dem Schlafengehen", "am Morgen". Es versteht sich, dass diese Begriffe in verschiedenen Kulturen möglicherweise nicht unbedingt den gleichen Zeiten entsprechen. Gegebenenfalls wird die Kodierung mit einer Terminologie empfohlen, z. B. mit HL7 Named Events.
     */
    @Path("/items[at0026 and name/value='Ereignis']/value|value")
    private String ereignisValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bestimmtes Ereignis/Ereignis/null_flavour
     */
    @Path("/items[at0026 and name/value='Ereignis']/null_flavour|defining_code")
    private NullFlavour ereignisNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bestimmtes Ereignis/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public String getEreignisValue() {
        return this.ereignisValue;
    }

    public void setEreignisValue(String ereignisValue) {
        this.ereignisValue = ereignisValue;
    }

    public NullFlavour getEreignisNullFlavourDefiningCode() {
        return this.ereignisNullFlavourDefiningCode;
    }

    public void setEreignisNullFlavourDefiningCode(NullFlavour ereignisNullFlavourDefiningCode) {
        this.ereignisNullFlavourDefiningCode = ereignisNullFlavourDefiningCode;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
