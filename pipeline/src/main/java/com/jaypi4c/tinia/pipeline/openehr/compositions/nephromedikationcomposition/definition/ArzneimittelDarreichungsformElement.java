package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.833064340+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public NullFlavour getValue2() {
        return this.value2;
    }

    public void setValue2(NullFlavour value2) {
        this.value2 = value2;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
