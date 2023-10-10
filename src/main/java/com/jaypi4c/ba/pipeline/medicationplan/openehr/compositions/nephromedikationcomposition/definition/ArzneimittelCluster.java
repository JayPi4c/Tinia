package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Archetype;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
@Archetype("openEHR-EHR-CLUSTER.medication.v1")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.832449618+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class ArzneimittelCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Darreichungsform
     * Description: Die Zusammensetzung oder die Darreichungsform eines Arzneimittels oder einer Arzneimittelkomponente.
     * Comment: Zum Beispiel: "Tablette", "Kapsel", "Creme", "Infusionslösung" oder "Inhalationspulver". Die Kodierung dieses Item mit einer Terminologie wird, sofern dies möglich ist, bevorzugt. Die Arzneimittelkataloge können zwischen der Darreichungsform "Injektionslösung" und der Produktform "Pulver zur Zubereitung der Injektionslösung" unterscheiden. Die exakte Zusammensetzung/Darreichungsform hängt vom Kontext der Anwendung ab. Es ist meist jedoch die Darreichungsform angegeben.
     */
    @Path("/items[at0071]")
    private List<ArzneimittelDarreichungsformElement> darreichungsform;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Wirkstoff
     * Description: Angaben über ein Arzneimittel oder eine Arzneimittelkomponente, einschließlich der Dosis, der Darreichungsform und jegliche Informationen über spezifische Inhaltsstoffe.
     */
    @Path("/items[openEHR-EHR-CLUSTER.medication.v1 and name/value='Wirkstoff']")
    private WirkstoffCluster wirkstoff;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/Strukturierte Angaben
     * Description: Zusätzliche Angaben über das Arzneimittel oder die Arzneimittelkomponente.
     * Comment: Zum Beispiel: Detaillierte Informationen über Wirkstoffgruppe oder vorgesehene Anwendungen, oder zusätzliche Informationen zum Verfallsdatum.
     */
    @Path("/items[at0141]")
    private List<Cluster> strukturierteAngaben;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public void setDarreichungsform(List<ArzneimittelDarreichungsformElement> darreichungsform) {
        this.darreichungsform = darreichungsform;
    }

    public List<ArzneimittelDarreichungsformElement> getDarreichungsform() {
        return this.darreichungsform;
    }

    public void setWirkstoff(WirkstoffCluster wirkstoff) {
        this.wirkstoff = wirkstoff;
    }

    public WirkstoffCluster getWirkstoff() {
        return this.wirkstoff;
    }

    public void setStrukturierteAngaben(List<Cluster> strukturierteAngaben) {
        this.strukturierteAngaben = strukturierteAngaben;
    }

    public List<Cluster> getStrukturierteAngaben() {
        return this.strukturierteAngaben;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }
}
