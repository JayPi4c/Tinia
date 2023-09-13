package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import java.lang.String;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.organisation.v0")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.876755465+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
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
     return this.einrichtungOrganisationValue ;
  }

  public void setEinrichtungOrganisationNullFlavourDefiningCode(
      NullFlavour einrichtungOrganisationNullFlavourDefiningCode) {
     this.einrichtungOrganisationNullFlavourDefiningCode = einrichtungOrganisationNullFlavourDefiningCode;
  }

  public NullFlavour getEinrichtungOrganisationNullFlavourDefiningCode() {
     return this.einrichtungOrganisationNullFlavourDefiningCode ;
  }

  public void setAdressangaben(Cluster adressangaben) {
     this.adressangaben = adressangaben;
  }

  public Cluster getAdressangaben() {
     return this.adressangaben ;
  }

  public void setTelekommunikationsdetails(Cluster telekommunikationsdetails) {
     this.telekommunikationsdetails = telekommunikationsdetails;
  }

  public Cluster getTelekommunikationsdetails() {
     return this.telekommunikationsdetails ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
