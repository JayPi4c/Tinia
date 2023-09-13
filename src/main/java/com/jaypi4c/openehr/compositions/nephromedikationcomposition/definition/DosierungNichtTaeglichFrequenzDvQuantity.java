package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import java.lang.Double;
import java.lang.String;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-09-13T13:58:55.863135149+02:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.29.0"
)
@OptionFor("DV_QUANTITY")
public class DosierungNichtTaeglichFrequenzDvQuantity implements RMEntity, DosierungNichtTaeglichFrequenzChoice {
  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/Frequenz
   * Description: Die Anzahl der Tage pro Zeitintervall, an denen die Aktivität stattfinden soll.
   * Comment: Zum Beispiel: "3 mal pro Woche", "2-4 mal pro Monat".
   */
  @Path("|magnitude")
  private Double frequenzMagnitude;

  /**
   * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/Frequenz
   * Description: Die Anzahl der Tage pro Zeitintervall, an denen die Aktivität stattfinden soll.
   * Comment: Zum Beispiel: "3 mal pro Woche", "2-4 mal pro Monat".
   */
  @Path("|units")
  private String frequenzUnits;

  public void setFrequenzMagnitude(Double frequenzMagnitude) {
     this.frequenzMagnitude = frequenzMagnitude;
  }

  public Double getFrequenzMagnitude() {
     return this.frequenzMagnitude ;
  }

  public void setFrequenzUnits(String frequenzUnits) {
     this.frequenzUnits = frequenzUnits;
  }

  public String getFrequenzUnits() {
     return this.frequenzUnits ;
  }
}
