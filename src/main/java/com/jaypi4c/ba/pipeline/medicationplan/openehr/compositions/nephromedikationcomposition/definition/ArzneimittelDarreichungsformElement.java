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
    date = "2023-09-13T13:58:55.831531065+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
public class ArzneimittelDarreichungsformElement implements LocatableEntity {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Darreichungsform
   * Description: Die Zusammensetzung oder die Darreichungsform eines Arzneimittels oder einer Arzneimittelkomponente.
   * Comment: Zum Beispiel: "Tablette", "Kapsel", "Creme", "Infusionslösung" oder "Inhalationspulver". Die Kodierung dieses Item mit einer Terminologie wird, sofern dies möglich ist, bevorzugt. Die Arzneimittelkataloge können zwischen der Darreichungsform "Injektionslösung" und der Produktform "Pulver zur Zubereitung der Injektionslösung" unterscheiden. Die exakte Zusammensetzung/Darreichungsform hängt vom Kontext der Anwendung ab. Es ist meist jedoch die Darreichungsform angegeben.
   */
  @Path("/value|value")
  private String value;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Arzneimittel/Darreichungsform/null_flavour
   */
  @Path("/null_flavour|defining_code")
  private NullFlavour value2;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setValue(String value) {
     this.value = value;
  }

  public String getValue() {
     return this.value ;
  }

  public void setValue2(NullFlavour value2) {
     this.value2 = value2;
  }

  public NullFlavour getValue2() {
     return this.value2 ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
