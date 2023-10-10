package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.857063759+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class DosierungNichtTaeglichBestimmtesEreignisCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmtes Ereignis/Ereignis
     * Description: Der Name des Ereignisses, das die Aktivität auslöst.
     * Comment: Dieses Element ist für Ereignisse gedacht, die zu verschiedenen Zeitpunkten auftreten können, wie z.B. Beginn der Menstruation, und nicht für Dosis oder Aktivitäten, die von einer anderen Variable abhängen. Bei Bedarf kann der Name des Ereignisses mit einer Terminologie kodiert werden, mit der die Anwendung ein bestimmtes Datum für die Aktivität festlegen kann.
     */
    @Path("/items[at0005 and name/value='Ereignis']/value|value")
    private String ereignisValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmtes Ereignis/Ereignis/null_flavour
     */
    @Path("/items[at0005 and name/value='Ereignis']/null_flavour|defining_code")
    private NullFlavour ereignisNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung - nicht täglich/Bestimmtes Ereignis/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public void setEreignisValue(String ereignisValue) {
        this.ereignisValue = ereignisValue;
    }

    public String getEreignisValue() {
        return this.ereignisValue;
    }

    public void setEreignisNullFlavourDefiningCode(NullFlavour ereignisNullFlavourDefiningCode) {
        this.ereignisNullFlavourDefiningCode = ereignisNullFlavourDefiningCode;
    }

    public NullFlavour getEreignisNullFlavourDefiningCode() {
        return this.ereignisNullFlavourDefiningCode;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }
}
