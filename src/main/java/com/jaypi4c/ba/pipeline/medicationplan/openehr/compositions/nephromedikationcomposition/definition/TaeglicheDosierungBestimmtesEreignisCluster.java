package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.String;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.851837119+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
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

  public void setEreignisValue(String ereignisValue) {
     this.ereignisValue = ereignisValue;
  }

  public String getEreignisValue() {
     return this.ereignisValue ;
  }

  public void setEreignisNullFlavourDefiningCode(NullFlavour ereignisNullFlavourDefiningCode) {
     this.ereignisNullFlavourDefiningCode = ereignisNullFlavourDefiningCode;
  }

  public NullFlavour getEreignisNullFlavourDefiningCode() {
     return this.ereignisNullFlavourDefiningCode ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
