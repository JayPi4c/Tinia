package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.Boolean;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Archetype("openEHR-EHR-CLUSTER.timing_daily.v1")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.850575060+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
public class TaeglicheDosierungCluster implements LocatableEntity {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bei Bedarf
   * Description: Als "Wahr" darstellen, wenn die Aktivität nur ausgeführt werden soll, wenn das "Kriterium "Bei Bedarf"" erfüllt ist.
   * Comment: In einigen Kulturen als "PRN" ("pro re nata", lateinisch: "unter den gegenwärtigen Umständen") oder "PN" ("per necessare", lateinisch: "wenn erforderlich") bezeichnet.
   */
  @Path("/items[at0024]/value|value")
  private Boolean beiBedarfValue;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bei Bedarf/null_flavour
   */
  @Path("/items[at0024]/null_flavour|defining_code")
  private NullFlavour beiBedarfNullFlavourDefiningCode;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/Bestimmtes Ereignis
   * Description: Ein bestimmtes, benanntes Zeitereignis, auf das sich die Aktivität bezieht und stattfinden soll.
   */
  @Path("/items[at0039]")
  private List<TaeglicheDosierungBestimmtesEreignisCluster> bestimmtesEreignis;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setBeiBedarfValue(Boolean beiBedarfValue) {
     this.beiBedarfValue = beiBedarfValue;
  }

  public Boolean isBeiBedarfValue() {
     return this.beiBedarfValue ;
  }

  public void setBeiBedarfNullFlavourDefiningCode(NullFlavour beiBedarfNullFlavourDefiningCode) {
     this.beiBedarfNullFlavourDefiningCode = beiBedarfNullFlavourDefiningCode;
  }

  public NullFlavour getBeiBedarfNullFlavourDefiningCode() {
     return this.beiBedarfNullFlavourDefiningCode ;
  }

  public void setBestimmtesEreignis(
      List<TaeglicheDosierungBestimmtesEreignisCluster> bestimmtesEreignis) {
     this.bestimmtesEreignis = bestimmtesEreignis;
  }

  public List<TaeglicheDosierungBestimmtesEreignisCluster> getBestimmtesEreignis() {
     return this.bestimmtesEreignis ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
