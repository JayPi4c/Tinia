package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;

@Entity
@Archetype("openEHR-EHR-CLUSTER.therapeutic_direction.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.847015782+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
public class TherapeutischeAnweisungCluster implements LocatableEntity {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung
   * Description: Kombination von Medikamentendosis und Verabreichungszeit an einem Tag im Kontext einer Medikamentenverordnung oder der Arzneimittelverwaltung.
   * Comment: Zum Beispiel: "2 Tabletten um 18 Uhr" oder "20 mg dreimal täglich". Bitte beachten Sie: Dieser Cluster kann mehrfach vorkommen, um einen vollständigen Satz von Dosismustern für eine einzelne Dosisanweisung darzustellen.
   */
  @Path("/items[openEHR-EHR-CLUSTER.dosage.v1]")
  private List<DosierungCluster> dosierung;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich
   * Description: Strukturierte Informationen über das geplante Zeitablaufmuster für eine therapeutische oder diagnostische Aktivität, die sich über Tage, Wochen, Monate oder Jahre erstreckt.
   */
  @Path("/items[openEHR-EHR-CLUSTER.timing_nondaily.v1 and name/value='Dosierung - nicht täglich']")
  private DosierungNichtTaeglichCluster dosierungNichtTaeglich;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Zusätzliche Angaben
   * Description: Weitere Angaben zu einer verordneten Produktanweisung.
   * Comment: Zum Beispiel bedingte Anweisungen wie Insulin- / Glukose-Infusionsrate basierend auf der Blutzuckermessung oder detaillierte Anweisungen für bestimmte Medikamente.
   */
  @Path("/items[at0156]")
  private List<Cluster> zusaetzlicheAngaben;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setDosierung(List<DosierungCluster> dosierung) {
     this.dosierung = dosierung;
  }

  public List<DosierungCluster> getDosierung() {
     return this.dosierung ;
  }

  public void setDosierungNichtTaeglich(DosierungNichtTaeglichCluster dosierungNichtTaeglich) {
     this.dosierungNichtTaeglich = dosierungNichtTaeglich;
  }

  public DosierungNichtTaeglichCluster getDosierungNichtTaeglich() {
     return this.dosierungNichtTaeglich ;
  }

  public void setZusaetzlicheAngaben(List<Cluster> zusaetzlicheAngaben) {
     this.zusaetzlicheAngaben = zusaetzlicheAngaben;
  }

  public List<Cluster> getZusaetzlicheAngaben() {
     return this.zusaetzlicheAngaben ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
