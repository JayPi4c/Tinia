package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Archetype;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
@Archetype("openEHR-EHR-CLUSTER.timing_daily.v1")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.847545155+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
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
        return this.beiBedarfValue;
    }

    public NullFlavour getBeiBedarfNullFlavourDefiningCode() {
        return this.beiBedarfNullFlavourDefiningCode;
    }

    public void setBeiBedarfNullFlavourDefiningCode(NullFlavour beiBedarfNullFlavourDefiningCode) {
        this.beiBedarfNullFlavourDefiningCode = beiBedarfNullFlavourDefiningCode;
    }

    public List<TaeglicheDosierungBestimmtesEreignisCluster> getBestimmtesEreignis() {
        return this.bestimmtesEreignis;
    }

    public void setBestimmtesEreignis(
            List<TaeglicheDosierungBestimmtesEreignisCluster> bestimmtesEreignis) {
        this.bestimmtesEreignis = bestimmtesEreignis;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
