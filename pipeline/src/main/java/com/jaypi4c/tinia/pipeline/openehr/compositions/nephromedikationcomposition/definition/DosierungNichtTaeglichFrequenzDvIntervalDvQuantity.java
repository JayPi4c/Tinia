package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.OptionFor;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.RMEntity;

import javax.annotation.processing.Generated;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.859378727+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
@OptionFor("DV_INTERVAL<DV_QUANTITY>")
public class DosierungNichtTaeglichFrequenzDvIntervalDvQuantity implements RMEntity, DosierungNichtTaeglichFrequenzChoice {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/upper
     */
    @Path("/upper|magnitude")
    private Double upperMagnitude;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/upper
     */
    @Path("/upper|units")
    private String upperUnits;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/lower
     */
    @Path("/lower|magnitude")
    private Double lowerMagnitude;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/lower
     */
    @Path("/lower|units")
    private String lowerUnits;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/lower_included
     */
    @Path("/lower_included")
    private Boolean lowerIncluded;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Frequenz/upper_included
     */
    @Path("/upper_included")
    private Boolean upperIncluded;

    public Double getUpperMagnitude() {
        return this.upperMagnitude;
    }

    public void setUpperMagnitude(Double upperMagnitude) {
        this.upperMagnitude = upperMagnitude;
    }

    public String getUpperUnits() {
        return this.upperUnits;
    }

    public void setUpperUnits(String upperUnits) {
        this.upperUnits = upperUnits;
    }

    public Double getLowerMagnitude() {
        return this.lowerMagnitude;
    }

    public void setLowerMagnitude(Double lowerMagnitude) {
        this.lowerMagnitude = lowerMagnitude;
    }

    public String getLowerUnits() {
        return this.lowerUnits;
    }

    public void setLowerUnits(String lowerUnits) {
        this.lowerUnits = lowerUnits;
    }

    public void setLowerIncluded(Boolean lowerIncluded) {
        this.lowerIncluded = lowerIncluded;
    }

    public Boolean isLowerIncluded() {
        return this.lowerIncluded;
    }

    public void setUpperIncluded(Boolean upperIncluded) {
        this.upperIncluded = upperIncluded;
    }

    public Boolean isUpperIncluded() {
        return this.upperIncluded;
    }
}
