package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Archetype;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;

@Entity
@Archetype("openEHR-EHR-CLUSTER.case_identification.v0")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.817733992+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class FallidentifikationCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/context/Fallidentifikation/Fall-Kennung
     * Description: Der Bezeichner/die Kennung dieses Falls.
     */
    @Path("/items[at0001]/value|value")
    private String fallKennungValue;

    /**
     * Path: Medikamentenliste/context/Tree/Fallidentifikation/Fall-Kennung/null_flavour
     */
    @Path("/items[at0001]/null_flavour|defining_code")
    private NullFlavour fallKennungNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/context/Fallidentifikation/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public String getFallKennungValue() {
        return this.fallKennungValue;
    }

    public void setFallKennungValue(String fallKennungValue) {
        this.fallKennungValue = fallKennungValue;
    }

    public NullFlavour getFallKennungNullFlavourDefiningCode() {
        return this.fallKennungNullFlavourDefiningCode;
    }

    public void setFallKennungNullFlavourDefiningCode(
            NullFlavour fallKennungNullFlavourDefiningCode) {
        this.fallKennungNullFlavourDefiningCode = fallKennungNullFlavourDefiningCode;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
