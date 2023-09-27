package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Language;

@Entity
@Archetype("openEHR-EHR-INSTRUCTION.medication_order.v2")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.820848458+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
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

  public void setVerordnung(List<VerordnungVonArzneimittelVerordnungActivity> verordnung) {
     this.verordnung = verordnung;
  }

  public List<VerordnungVonArzneimittelVerordnungActivity> getVerordnung() {
     return this.verordnung ;
  }

  public void setAutorEintrag(AutorEintragCluster autorEintrag) {
     this.autorEintrag = autorEintrag;
  }

  public AutorEintragCluster getAutorEintrag() {
     return this.autorEintrag ;
  }

  public void setSubject(PartyProxy subject) {
     this.subject = subject;
  }

  public PartyProxy getSubject() {
     return this.subject ;
  }

  public void setNarrativeValue(String narrativeValue) {
     this.narrativeValue = narrativeValue;
  }

  public String getNarrativeValue() {
     return this.narrativeValue ;
  }

  public void setLanguage(Language language) {
     this.language = language;
  }

  public Language getLanguage() {
     return this.language ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }

  public void setExpiryTimeValue(TemporalAccessor expiryTimeValue) {
     this.expiryTimeValue = expiryTimeValue;
  }

  public TemporalAccessor getExpiryTimeValue() {
     return this.expiryTimeValue ;
  }
}
