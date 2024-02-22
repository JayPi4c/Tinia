package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.PartyProxy;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Archetype;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.EntryEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;

import javax.annotation.processing.Generated;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Entity
@Archetype("openEHR-EHR-INSTRUCTION.medication_order.v2")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.827113949+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class VerordnungVonArzneimittelInstruction implements EntryEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung
     * Description: Angaben zur Verordnung.
     */
    @Path("/activities[at0001]")
    private List<VerordnungVonArzneimittelVerordnungActivity> verordnung;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Autor Eintrag
     * Description: Angaben zu einer Einrichtung
     */
    @Path("/protocol[at0005]/items[openEHR-EHR-CLUSTER.organisation.v0 and name/value='Autor Eintrag']")
    private AutorEintragCluster autorEintrag;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/subject
     */
    @Path("/subject")
    private PartyProxy subject;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/narrative
     */
    @Path("/narrative|value")
    private String narrativeValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/language
     */
    @Path("/language")
    private Language language;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/expiry_time
     */
    @Path("/expiry_time|value")
    private TemporalAccessor expiryTimeValue;

    public List<VerordnungVonArzneimittelVerordnungActivity> getVerordnung() {
        return this.verordnung;
    }

    public void setVerordnung(List<VerordnungVonArzneimittelVerordnungActivity> verordnung) {
        this.verordnung = verordnung;
    }

    public AutorEintragCluster getAutorEintrag() {
        return this.autorEintrag;
    }

    public void setAutorEintrag(AutorEintragCluster autorEintrag) {
        this.autorEintrag = autorEintrag;
    }

    public PartyProxy getSubject() {
        return this.subject;
    }

    public void setSubject(PartyProxy subject) {
        this.subject = subject;
    }

    public String getNarrativeValue() {
        return this.narrativeValue;
    }

    public void setNarrativeValue(String narrativeValue) {
        this.narrativeValue = narrativeValue;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }

    public TemporalAccessor getExpiryTimeValue() {
        return this.expiryTimeValue;
    }

    public void setExpiryTimeValue(TemporalAccessor expiryTimeValue) {
        this.expiryTimeValue = expiryTimeValue;
    }
}
