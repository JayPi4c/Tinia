package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.OptionFor;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.RMEntity;

import javax.annotation.processing.Generated;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.858669926+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
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

    public Double getFrequenzMagnitude() {
        return this.frequenzMagnitude;
    }

    public void setFrequenzMagnitude(Double frequenzMagnitude) {
        this.frequenzMagnitude = frequenzMagnitude;
    }

    public String getFrequenzUnits() {
        return this.frequenzUnits;
    }

    public void setFrequenzUnits(String frequenzUnits) {
        this.frequenzUnits = frequenzUnits;
    }
}
