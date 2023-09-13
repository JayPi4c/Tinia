package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.String;
import java.time.temporal.TemporalAmount;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Choice;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.timing_nondaily.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.855132334+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
public class DosierungNichtTaeglichCluster implements LocatableEntity {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Wiederholungsintervall
   * Description: Das Intervall zwischen den Wiederholungen der Aktivität.
   * Comment: Zum Beispiel: "Alle 3 Wochen". Falls erforderlich, kann mit diesem Element explizit angegeben werden, dass eine Aktivität jeden einzelnen Tag stattfinden soll, indem es auf "1 Tag" gesetzt wird.
   */
  @Path("/items[at0002]/value|value")
  private TemporalAmount wiederholungsintervallValue;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung - nicht täglich/Wiederholungsintervall/null_flavour
   */
  @Path("/items[at0002]/null_flavour|defining_code")
  private NullFlavour wiederholungsintervallNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/null_flavour
   */
  @Path("/items[at0014]/null_flavour|defining_code")
  private NullFlavour frequenzNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmter Wochentag
   * Description: Die Aktivität sollte an einem bestimmten Wochentag stattfinden.
   * Comment: Zum Beispiel: "Am Montag, Mittwoch und Freitag".
   */
  @Path("/items[at0003]/value|defining_code")
  private BestimmterWochentagDefiningCode bestimmterWochentagDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmter Wochentag/null_flavour
   */
  @Path("/items[at0003]/null_flavour|defining_code")
  private NullFlavour bestimmterWochentagNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Beschreibung des Zeitablaufs
   * Description: Freitextbeschreibung des Zeitablaufs.
   * Comment: Zum Beispiel: "Eine Woche verwenden, dann zwei Wochen pausieren und dann wiederholen". Dieses Element soll es ermöglichen, die Strukturen für tägliche Zeitabläufe zu verwenden, ohne notwendigerweise die Struktur der nicht-täglichen Zeitabläufe angeben zu müssen.
   */
  @Path("/items[at0021]/value|value")
  private String beschreibungDesZeitablaufsValue;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung - nicht täglich/Beschreibung des Zeitablaufs/null_flavour
   */
  @Path("/items[at0021]/null_flavour|defining_code")
  private NullFlavour beschreibungDesZeitablaufsNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmtes Ereignis
   * Description: Ein bestimmtes, benanntes Zeitereignis, auf das sich die Aktivität bezieht und stattfinden soll.
   */
  @Path("/items[at0006]")
  private List<DosierungNichtTaeglichBestimmtesEreignisCluster> bestimmtesEreignis;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz
   * Description: Die Anzahl der Tage pro Zeitintervall, an denen die Aktivität stattfinden soll.
   * Comment: Zum Beispiel: "3 mal pro Woche", "2-4 mal pro Monat".
   */
  @Path("/items[at0014]/value")
  @Choice
  private DosierungNichtTaeglichFrequenzChoice frequenz;

  public void setWiederholungsintervallValue(TemporalAmount wiederholungsintervallValue) {
     this.wiederholungsintervallValue = wiederholungsintervallValue;
  }

  public TemporalAmount getWiederholungsintervallValue() {
     return this.wiederholungsintervallValue ;
  }

  public void setWiederholungsintervallNullFlavourDefiningCode(
      NullFlavour wiederholungsintervallNullFlavourDefiningCode) {
     this.wiederholungsintervallNullFlavourDefiningCode = wiederholungsintervallNullFlavourDefiningCode;
  }

  public NullFlavour getWiederholungsintervallNullFlavourDefiningCode() {
     return this.wiederholungsintervallNullFlavourDefiningCode ;
  }

  public void setFrequenzNullFlavourDefiningCode(NullFlavour frequenzNullFlavourDefiningCode) {
     this.frequenzNullFlavourDefiningCode = frequenzNullFlavourDefiningCode;
  }

  public NullFlavour getFrequenzNullFlavourDefiningCode() {
     return this.frequenzNullFlavourDefiningCode ;
  }

  public void setBestimmterWochentagDefiningCode(
      BestimmterWochentagDefiningCode bestimmterWochentagDefiningCode) {
     this.bestimmterWochentagDefiningCode = bestimmterWochentagDefiningCode;
  }

  public BestimmterWochentagDefiningCode getBestimmterWochentagDefiningCode() {
     return this.bestimmterWochentagDefiningCode ;
  }

  public void setBestimmterWochentagNullFlavourDefiningCode(
      NullFlavour bestimmterWochentagNullFlavourDefiningCode) {
     this.bestimmterWochentagNullFlavourDefiningCode = bestimmterWochentagNullFlavourDefiningCode;
  }

  public NullFlavour getBestimmterWochentagNullFlavourDefiningCode() {
     return this.bestimmterWochentagNullFlavourDefiningCode ;
  }

  public void setBeschreibungDesZeitablaufsValue(String beschreibungDesZeitablaufsValue) {
     this.beschreibungDesZeitablaufsValue = beschreibungDesZeitablaufsValue;
  }

  public String getBeschreibungDesZeitablaufsValue() {
     return this.beschreibungDesZeitablaufsValue ;
  }

  public void setBeschreibungDesZeitablaufsNullFlavourDefiningCode(
      NullFlavour beschreibungDesZeitablaufsNullFlavourDefiningCode) {
     this.beschreibungDesZeitablaufsNullFlavourDefiningCode = beschreibungDesZeitablaufsNullFlavourDefiningCode;
  }

  public NullFlavour getBeschreibungDesZeitablaufsNullFlavourDefiningCode() {
     return this.beschreibungDesZeitablaufsNullFlavourDefiningCode ;
  }

  public void setBestimmtesEreignis(
      List<DosierungNichtTaeglichBestimmtesEreignisCluster> bestimmtesEreignis) {
     this.bestimmtesEreignis = bestimmtesEreignis;
  }

  public List<DosierungNichtTaeglichBestimmtesEreignisCluster> getBestimmtesEreignis() {
     return this.bestimmtesEreignis ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }

  public void setFrequenz(DosierungNichtTaeglichFrequenzChoice frequenz) {
     this.frequenz = frequenz;
  }

  public DosierungNichtTaeglichFrequenzChoice getFrequenz() {
     return this.frequenz ;
  }
}
