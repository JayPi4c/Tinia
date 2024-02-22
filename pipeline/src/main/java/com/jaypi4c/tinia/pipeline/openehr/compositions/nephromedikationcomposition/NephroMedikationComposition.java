package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition;

import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.FallidentifikationCluster;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.VerordnungVonArzneimittelInstruction;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import com.nedap.archie.rm.support.identification.ObjectVersionId;
import org.ehrbase.openehr.sdk.generator.commons.annotations.*;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Category;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;

import javax.annotation.processing.Generated;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Entity
@Archetype("openEHR-EHR-COMPOSITION.medication_list.v1")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.804477898+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
@Template("Nephro_Medikation")
public class NephroMedikationComposition implements CompositionEntity {
    /**
     * Path: Medikamentenliste/category
     */
    @Path("/category|defining_code")
    private Category categoryDefiningCode;

    /**
     * Path: Medikamentenliste/context/Fallidentifikation
     * Description: Zur Erfassung von Details zur Identifikation eines Falls im Gesundheitswesen.
     */
    @Path("/context/other_context[at0005]/items[openEHR-EHR-CLUSTER.case_identification.v0]")
    private FallidentifikationCluster fallidentifikation;

    /**
     * Path: Medikamentenliste/context/start_time
     */
    @Path("/context/start_time|value")
    private TemporalAccessor startTimeValue;

    /**
     * Path: Medikamentenliste/context/participations
     */
    @Path("/context/participations")
    private List<Participation> participations;

    /**
     * Path: Medikamentenliste/context/end_time
     */
    @Path("/context/end_time|value")
    private TemporalAccessor endTimeValue;

    /**
     * Path: Medikamentenliste/context/location
     */
    @Path("/context/location")
    private String location;

    /**
     * Path: Medikamentenliste/context/health_care_facility
     */
    @Path("/context/health_care_facility")
    private PartyIdentified healthCareFacility;

    /**
     * Path: Medikamentenliste/context/setting
     */
    @Path("/context/setting|defining_code")
    private Setting settingDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel
     * Description: Verschreibung eines Medikaments, Impfstoffs, Ernährungsprodukts oder anderen therapeutischen Mittels für eine bestimmte Person.
     */
    @Path("/content[openEHR-EHR-INSTRUCTION.medication_order.v2]")
    private List<VerordnungVonArzneimittelInstruction> verordnungVonArzneimittel;

    /**
     * Path: Medikamentenliste/composer
     */
    @Path("/composer")
    private PartyProxy composer;

    /**
     * Path: Medikamentenliste/language
     */
    @Path("/language")
    private Language language;

    /**
     * Path: Medikamentenliste/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    /**
     * Path: Medikamentenliste/territory
     */
    @Path("/territory")
    private Territory territory;

    @Id
    private ObjectVersionId versionUid;

    public Category getCategoryDefiningCode() {
        return this.categoryDefiningCode;
    }

    public void setCategoryDefiningCode(Category categoryDefiningCode) {
        this.categoryDefiningCode = categoryDefiningCode;
    }

    public FallidentifikationCluster getFallidentifikation() {
        return this.fallidentifikation;
    }

    public void setFallidentifikation(FallidentifikationCluster fallidentifikation) {
        this.fallidentifikation = fallidentifikation;
    }

    public TemporalAccessor getStartTimeValue() {
        return this.startTimeValue;
    }

    public void setStartTimeValue(TemporalAccessor startTimeValue) {
        this.startTimeValue = startTimeValue;
    }

    public List<Participation> getParticipations() {
        return this.participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public TemporalAccessor getEndTimeValue() {
        return this.endTimeValue;
    }

    public void setEndTimeValue(TemporalAccessor endTimeValue) {
        this.endTimeValue = endTimeValue;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PartyIdentified getHealthCareFacility() {
        return this.healthCareFacility;
    }

    public void setHealthCareFacility(PartyIdentified healthCareFacility) {
        this.healthCareFacility = healthCareFacility;
    }

    public Setting getSettingDefiningCode() {
        return this.settingDefiningCode;
    }

    public void setSettingDefiningCode(Setting settingDefiningCode) {
        this.settingDefiningCode = settingDefiningCode;
    }

    public List<VerordnungVonArzneimittelInstruction> getVerordnungVonArzneimittel() {
        return this.verordnungVonArzneimittel;
    }

    public void setVerordnungVonArzneimittel(
            List<VerordnungVonArzneimittelInstruction> verordnungVonArzneimittel) {
        this.verordnungVonArzneimittel = verordnungVonArzneimittel;
    }

    public PartyProxy getComposer() {
        return this.composer;
    }

    public void setComposer(PartyProxy composer) {
        this.composer = composer;
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

    public Territory getTerritory() {
        return this.territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public ObjectVersionId getVersionUid() {
        return this.versionUid;
    }

    public void setVersionUid(ObjectVersionId versionUid) {
        this.versionUid = versionUid;
    }
}
