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
@Archetype("openEHR-EHR-CLUSTER.dosage.v1")
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.844669904+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class DosierungCluster implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Dosismenge
     * Description: Der Wert der Arzneimittelmenge, die an einem Zeitpunkt verabreicht wird, als reelle Zahl oder als Bereich von reellen Zahlen. Dem Wert ist die Dosiseinheit zugeordnet.
     * Comment: Zum Beispiel: 1; 1,5; 0,125 oder 1-2; 12,5 - 20,5
     */
    @Path("/items[at0144]/value|magnitude")
    private Double dosismengeMagnitude;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Dosismenge
     * Description: Der Wert der Arzneimittelmenge, die an einem Zeitpunkt verabreicht wird, als reelle Zahl oder als Bereich von reellen Zahlen. Dem Wert ist die Dosiseinheit zugeordnet.
     * Comment: Zum Beispiel: 1; 1,5; 0,125 oder 1-2; 12,5 - 20,5
     */
    @Path("/items[at0144]/value|units")
    private String dosismengeUnits;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung/Dosismenge/null_flavour
     */
    @Path("/items[at0144]/null_flavour|defining_code")
    private NullFlavour dosismengeNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Dosiseinheit
     * Description: Die Einheit, die der Dosismenge zugeordnet ist.
     * Comment: Zum Beispiel: "Tablette", "mg". Die Kodierung der Dosiseinheit mit einer Terminologie wird nach Möglichkeit bevorzugt.
     */
    @Path("/items[at0145]/value|value")
    private String dosiseinheitValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung/Dosiseinheit/null_flavour
     */
    @Path("/items[at0145]/null_flavour|defining_code")
    private NullFlavour dosiseinheitNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Dosierung Freitext
     * Description: Textbeschreibung der Dosis.
     * Comment: Zum Beispiel: "Salbe auf die betroffene Stelle auftragen, bis sie glänzt". Dieses Element soll den Entwicklern ermöglichen, die Strukturen zum Erhöhen / Verringern von Dosierungen zu verwenden, ohne die Dosierungen notwendigerweise auf strukturierte Weise spezifizieren zu müssen.
     */
    @Path("/items[at0178 and name/value='Dosierung Freitext']/value|value")
    private String dosierungFreitextValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Therapeutische Anweisung/Dosierung/Dosierung Freitext/null_flavour
     */
    @Path("/items[at0178 and name/value='Dosierung Freitext']/null_flavour|defining_code")
    private NullFlavour dosierungFreitextNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/Tägliche Dosierung
     * Description: Strukturierte Informationen über den geplanten Zeitablauf einer therapeutischen oder diagnostischen Aktivität innerhalb eines Zeitraums von 24 Stunden.
     */
    @Path("/items[openEHR-EHR-CLUSTER.timing_daily.v1 and name/value='Tägliche Dosierung']")
    private List<TaeglicheDosierungCluster> taeglicheDosierung;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung/Dosierung/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public Double getDosismengeMagnitude() {
        return this.dosismengeMagnitude;
    }

    public void setDosismengeMagnitude(Double dosismengeMagnitude) {
        this.dosismengeMagnitude = dosismengeMagnitude;
    }

    public String getDosismengeUnits() {
        return this.dosismengeUnits;
    }

    public void setDosismengeUnits(String dosismengeUnits) {
        this.dosismengeUnits = dosismengeUnits;
    }

    public NullFlavour getDosismengeNullFlavourDefiningCode() {
        return this.dosismengeNullFlavourDefiningCode;
    }

    public void setDosismengeNullFlavourDefiningCode(NullFlavour dosismengeNullFlavourDefiningCode) {
        this.dosismengeNullFlavourDefiningCode = dosismengeNullFlavourDefiningCode;
    }

    public String getDosiseinheitValue() {
        return this.dosiseinheitValue;
    }

    public void setDosiseinheitValue(String dosiseinheitValue) {
        this.dosiseinheitValue = dosiseinheitValue;
    }

    public NullFlavour getDosiseinheitNullFlavourDefiningCode() {
        return this.dosiseinheitNullFlavourDefiningCode;
    }

    public void setDosiseinheitNullFlavourDefiningCode(
            NullFlavour dosiseinheitNullFlavourDefiningCode) {
        this.dosiseinheitNullFlavourDefiningCode = dosiseinheitNullFlavourDefiningCode;
    }

    public String getDosierungFreitextValue() {
        return this.dosierungFreitextValue;
    }

    public void setDosierungFreitextValue(String dosierungFreitextValue) {
        this.dosierungFreitextValue = dosierungFreitextValue;
    }

    public NullFlavour getDosierungFreitextNullFlavourDefiningCode() {
        return this.dosierungFreitextNullFlavourDefiningCode;
    }

    public void setDosierungFreitextNullFlavourDefiningCode(
            NullFlavour dosierungFreitextNullFlavourDefiningCode) {
        this.dosierungFreitextNullFlavourDefiningCode = dosierungFreitextNullFlavourDefiningCode;
    }

    public List<TaeglicheDosierungCluster> getTaeglicheDosierung() {
        return this.taeglicheDosierung;
    }

    public void setTaeglicheDosierung(List<TaeglicheDosierungCluster> taeglicheDosierung) {
        this.taeglicheDosierung = taeglicheDosierung;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
