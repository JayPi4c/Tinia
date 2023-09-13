package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import java.lang.Double;
import java.lang.String;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.medication.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.833679150+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
public class WirkstoffCluster implements LocatableEntity {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/Wirkstoff
   * Description: Der Name des Arzneimittels oder der Arzneimittelkomponente.
   * Comment: Zum Beispiel: "Zinacef 750 mg Puder" oder "Cefuroxim". Dieses Item sollte möglichst kodiert werden, z.B. mittels RxNorm, DM+D, Australische Arzneimittelterminologie oder FEST. Die Verwendung dieses Elements variiert je nach Anwendungskontext. Dieses Element kann weggelassen werden, wenn der Name des Arzneimittels in dem übergeordneten INSTRUCTION- oder ACTION-Archetyp eingetragen ist, und dieser Archetyp nur zur Dokumentation der Darreichungsform/Verwendungsart genutzt wird, z.B. "flüssig".
   */
  @Path("/items[at0132 and name/value='Wirkstoff']/value|value")
  private String wirkstoffValue;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Arzneimittel/Wirkstoff/Wirkstoff/null_flavour
   */
  @Path("/items[at0132 and name/value='Wirkstoff']/null_flavour|defining_code")
  private NullFlavour wirkstoffNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/Wirkstoffstärke
   * Description: Die Wirkstoffkonzentration eines Arzneimittels oder einer Arzneimittelkomponente.
   * Comment: Dieses Element wird für Flüssigkeiten, halbfesten Arzneimitteln oder Arzneimitteln, die vor der Verabreichung in einer Flüssigkeit gelöst werden müssen, verwendet. Zum Beispiel: "10 mg/ml", "20 mg/g", "5 %", "10,000 SQ-U/ml".
   */
  @Path("/items[at0115 and name/value='Wirkstoffstärke']/value|magnitude")
  private Double wirkstoffstaerkeMagnitude;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/Wirkstoffstärke
   * Description: Die Wirkstoffkonzentration eines Arzneimittels oder einer Arzneimittelkomponente.
   * Comment: Dieses Element wird für Flüssigkeiten, halbfesten Arzneimitteln oder Arzneimitteln, die vor der Verabreichung in einer Flüssigkeit gelöst werden müssen, verwendet. Zum Beispiel: "10 mg/ml", "20 mg/g", "5 %", "10,000 SQ-U/ml".
   */
  @Path("/items[at0115 and name/value='Wirkstoffstärke']/value|units")
  private String wirkstoffstaerkeUnits;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Arzneimittel/Wirkstoff/Wirkstoffstärke/null_flavour
   */
  @Path("/items[at0115 and name/value='Wirkstoffstärke']/null_flavour|defining_code")
  private NullFlavour wirkstoffstaerkeNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/Bestandteil
   * Description: Angaben zu einem Inhaltsstoff oder Produkt, das zur Herstellung einer Mischpackung, eines Präparats oder einer Infusion verwendet wird.
   * Comment: Dieser Slot ist dazu gedacht, dem Archetyp Details über Bestandteile des Arzneimittels oder der Arzneimittelkomponente unter Verwendung verschachtelter Instanzen hinzuzufügen. Dies ist in der Regel nur dann erforderlich, wenn eine Mixtur beschrieben wird.
   */
  @Path("/items[at0138]")
  private List<Cluster> bestandteil;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/Strukturierte Angaben
   * Description: Zusätzliche Angaben über das Arzneimittel oder die Arzneimittelkomponente.
   * Comment: Zum Beispiel: Detaillierte Informationen über Wirkstoffgruppe oder vorgesehene Anwendungen, oder zusätzliche Informationen zum Verfallsdatum.
   */
  @Path("/items[at0141]")
  private List<Cluster> strukturierteAngaben;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setWirkstoffValue(String wirkstoffValue) {
     this.wirkstoffValue = wirkstoffValue;
  }

  public String getWirkstoffValue() {
     return this.wirkstoffValue ;
  }

  public void setWirkstoffNullFlavourDefiningCode(NullFlavour wirkstoffNullFlavourDefiningCode) {
     this.wirkstoffNullFlavourDefiningCode = wirkstoffNullFlavourDefiningCode;
  }

  public NullFlavour getWirkstoffNullFlavourDefiningCode() {
     return this.wirkstoffNullFlavourDefiningCode ;
  }

  public void setWirkstoffstaerkeMagnitude(Double wirkstoffstaerkeMagnitude) {
     this.wirkstoffstaerkeMagnitude = wirkstoffstaerkeMagnitude;
  }

  public Double getWirkstoffstaerkeMagnitude() {
     return this.wirkstoffstaerkeMagnitude ;
  }

  public void setWirkstoffstaerkeUnits(String wirkstoffstaerkeUnits) {
     this.wirkstoffstaerkeUnits = wirkstoffstaerkeUnits;
  }

  public String getWirkstoffstaerkeUnits() {
     return this.wirkstoffstaerkeUnits ;
  }

  public void setWirkstoffstaerkeNullFlavourDefiningCode(
      NullFlavour wirkstoffstaerkeNullFlavourDefiningCode) {
     this.wirkstoffstaerkeNullFlavourDefiningCode = wirkstoffstaerkeNullFlavourDefiningCode;
  }

  public NullFlavour getWirkstoffstaerkeNullFlavourDefiningCode() {
     return this.wirkstoffstaerkeNullFlavourDefiningCode ;
  }

  public void setBestandteil(List<Cluster> bestandteil) {
     this.bestandteil = bestandteil;
  }

  public List<Cluster> getBestandteil() {
     return this.bestandteil ;
  }

  public void setStrukturierteAngaben(List<Cluster> strukturierteAngaben) {
     this.strukturierteAngaben = strukturierteAngaben;
  }

  public List<Cluster> getStrukturierteAngaben() {
     return this.strukturierteAngaben ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
