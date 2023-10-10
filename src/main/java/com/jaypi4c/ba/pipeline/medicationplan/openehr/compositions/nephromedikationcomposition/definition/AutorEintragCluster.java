package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Archetype;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;

@Entity
@Archetype("openEHR-EHR-CLUSTER.organisation.v0")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.871550075+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class AutorEintragCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Autor Eintrag/Einrichtung/Organisation
     * Description: Name der Einrichtung
     */
    @Path("/items[at0001 and name/value='Einrichtung/Organisation']/value|value")
    private String einrichtungOrganisationValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Beschreibung der Dosismenge/Autor Eintrag/Einrichtung/Organisation/null_flavour
     */
    @Path("/items[at0001 and name/value='Einrichtung/Organisation']/null_flavour|defining_code")
    private NullFlavour einrichtungOrganisationNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Autor Eintrag/Adressangaben
     * Description: Angaben zur Adresse
     */
    @Path("/items[at0008]")
    private Cluster adressangaben;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Autor Eintrag/Telekommunikationsdetails
     * Description: Details zur Telekommunikation.
     */
    @Path("/items[at0009]")
    private Cluster telekommunikationsdetails;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Autor Eintrag/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public void setEinrichtungOrganisationValue(String einrichtungOrganisationValue) {
        this.einrichtungOrganisationValue = einrichtungOrganisationValue;
    }

    public String getEinrichtungOrganisationValue() {
        return this.einrichtungOrganisationValue;
    }

    public void setEinrichtungOrganisationNullFlavourDefiningCode(
            NullFlavour einrichtungOrganisationNullFlavourDefiningCode) {
        this.einrichtungOrganisationNullFlavourDefiningCode = einrichtungOrganisationNullFlavourDefiningCode;
    }

    public NullFlavour getEinrichtungOrganisationNullFlavourDefiningCode() {
        return this.einrichtungOrganisationNullFlavourDefiningCode;
    }

    public void setAdressangaben(Cluster adressangaben) {
        this.adressangaben = adressangaben;
    }

    public Cluster getAdressangaben() {
        return this.adressangaben;
    }

    public void setTelekommunikationsdetails(Cluster telekommunikationsdetails) {
        this.telekommunikationsdetails = telekommunikationsdetails;
    }

    public Cluster getTelekommunikationsdetails() {
        return this.telekommunikationsdetails;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }
}
