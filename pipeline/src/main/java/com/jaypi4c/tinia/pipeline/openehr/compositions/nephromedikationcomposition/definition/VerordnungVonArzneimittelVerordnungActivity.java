package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.datavalues.encapsulated.DvParsable;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Entity;
import org.ehrbase.openehr.sdk.generator.commons.annotations.Path;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.LocatableEntity;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

import javax.annotation.processing.Generated;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Entity
@Generated(
        value = "org.ehrbase.openehr.sdk.generator.ClassGenerator",
        date = "2023-10-10T14:34:10.829012671+02:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 2.4.0"
)
public class VerordnungVonArzneimittelVerordnungActivity implements LocatableEntity {
    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Handelsname
     * Description: Name des verordneten Medikaments, Impfstoffs oder anderen therapeutischen / verschreibungsfähigen Mittels.
     * Comment: Je nach Kontext kann dieses Feld entweder für die generische oder die produktbasierte Verschreibung verwendet werden. In diesem Datenelement können eng gebundene Verordnungen verschiedener Medikamente dargestellt werden, wenn diese als Einzelpackung verschrieben werden. Es wird dringend empfohlen, das „Arzneimittel“ mit einer Terminologie zu kodieren, die nach Möglichkeit eine Entscheidungsunterstützung triggern kann. Der Umfang der Kodierung kann vom einfachen Medikamentennamen bis zu strukturierten Details über die tatsächlich zu verwendende Medikamentenpackung variieren.
     * Die Freitexteingabe sollte nur verwendet werden, wenn keine entsprechende Terminologie verfügbar ist.
     */
    @Path("/description[at0002]/items[at0070 and name/value='Handelsname']/value|value")
    private String handelsnameValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Handelsname/null_flavour
     */
    @Path("/description[at0002]/items[at0070 and name/value='Handelsname']/null_flavour|defining_code")
    private NullFlavour handelsnameNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Arzneimittel
     * Description: Angaben über ein Arzneimittel oder eine Arzneimittelkomponente, einschließlich der Dosis, der Darreichungsform und jegliche Informationen über spezifische Inhaltsstoffe.
     */
    @Path("/description[at0002]/items[openEHR-EHR-CLUSTER.medication.v1]")
    private ArzneimittelCluster arzneimittel;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Lokalisation
     * Description: Strukturierte Beschreibung der Körperstelle, an der das Arzneimittel verabreicht wird.
     */
    @Path("/description[at0002]/items[at0093]")
    private Cluster lokalisation;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Verabreichungsmittel
     * Description: Details zu der Vorrichtung mit der das verordnete Arzneimittel verabreicht wird.
     */
    @Path("/description[at0002]/items[at0095]")
    private List<Cluster> verabreichungsmittel;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Strukturierte Dosis- und Zeitangaben
     * Description: Details zu strukturierten Dosis- und Zeitangaben
     */
    @Path("/description[at0002]/items[at0177]")
    private List<Cluster> strukturierteDosisUndZeitangaben;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Hinweise zur Einnahme
     * Description: Ein zusätzlicher Hinweis zur Verwendung oder Lagerung des verordneten Arzneimittels.
     * Comment: Zum Beispiel: Vorsichtsmaßnahmen wie "Mit dem Essen nehmen", "Grapefruit vermeiden", "In Wasser auflösen", "An einem kühlen, trockenen Ort aufbewahren". Dieses Datenelement kann mehrfach vorkommen und sollte nach Möglichkeit mit einer Referenz-Terminologie kodiert werden.
     */
    @Path("/description[at0002]/items[at0044 and name/value='Hinweise zur Einnahme']/value|value")
    private String hinweiseZurEinnahmeValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Hinweise zur Einnahme/null_flavour
     */
    @Path("/description[at0002]/items[at0044 and name/value='Hinweise zur Einnahme']/null_flavour|defining_code")
    private NullFlavour hinweiseZurEinnahmeNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Einnahmegrund
     * Description: Der kliinische Grund für die Verwendung des verordneten Arzneimittels.
     * Comment: Zum Beispiel: "Angina". Die Kodierung der klinischen Indikation mit einer Terminologie wird nach Möglichkeit bevorzugt. Dieses Datenelement kann mehrfach vorkommen. Es ist nicht beabsichtigt, eine Angabe für behördliche Genehmigungszwecke mitzuführen.
     */
    @Path("/description[at0002]/items[at0018 and name/value='Einnahmegrund']/value|value")
    private String einnahmegrundValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Einnahmegrund/null_flavour
     */
    @Path("/description[at0002]/items[at0018 and name/value='Einnahmegrund']/null_flavour|defining_code")
    private NullFlavour einnahmegrundNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Detaillierte Angaben zu der Verordnung/Datum der Verordnung
     * Description: Datum und optionale Uhrzeit für den Beginn der Verwendung des verordneten Arzneimittels.
     */
    @Path("/description[at0002]/items[at0113]/items[at0012 and name/value='Datum der Verordnung']/value|value")
    private TemporalAccessor datumDerVerordnungValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Detaillierte Angaben zu der Verordnung/Datum der Verordnung/null_flavour
     */
    @Path("/description[at0002]/items[at0113]/items[at0012 and name/value='Datum der Verordnung']/null_flavour|defining_code")
    private NullFlavour datumDerVerordnungNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Detaillierte Angaben zu der Verordnung/Zusammenfassung der Verordnung
     * Description: Zusammenfassende Informationen zur Verwendung des verordneten Arzneimittels, z. B. aktueller Status oder Schlüsseldaten, die normalerweise in anderen Kontexten außer Verordnung verwendet werden.
     * Comment: Eine Zusammenfassung der Verordnung kann erforderlich sein, wenn Arzneimittelinformationen zwischen Systemen übertragen oder eine FHIR- "Medication Statement" dargestellt werden. Zum Beispiel: Als Teil einer Überweisung, einer Übersicht über Notfallpatienten oder Kommunikation bei der Entlassung.
     */
    @Path("/description[at0002]/items[at0113]/items[at0112]")
    private List<Cluster> zusammenfassungDerVerordnung;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Anweisungen zur Genehmigung
     * Description: Details der Autorisierung für das verordnete Arzneimittel, einschließlich Unterstützung der lokalen Richtlinien zur Selbstverwaltung, Ausgabe und Bestätigung.
     * Comment: Zum Beispiel: Angaben über Wiederholung oder Nachbereitung. Dieser SLOT ermöglicht lokale Unterschiede zwischen verschiedenen Rechtsordnungen hinsichtlich der Genehmigung und Re-Autorisierung von Arzneimitteln zu verwalten.
     */
    @Path("/description[at0002]/items[at0069]")
    private List<Cluster> anweisungenZurGenehmigung;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Therapeutische Anweisung
     * Description: Strukturierte Informationen über eine einzelne therapeutische Anweisung / Anleitung für ein verordnetes Produkt, z. B. eine Medikamenten- oder Bluttransfusionsverordnung.
     */
    @Path("/description[at0002]/items[openEHR-EHR-CLUSTER.therapeutic_direction.v1]")
    private List<TherapeutischeAnweisungCluster> therapeutischeAnweisung;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Kommentar
     * Description: Zusätzliche Kommentare über die Medikamentenverordnung, die nicht in anderen Feldern erfasst wurden.
     */
    @Path("/description[at0002]/items[at0167]/value|value")
    private String kommentarValue;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/Tree/Kommentar/null_flavour
     */
    @Path("/description[at0002]/items[at0167]/null_flavour|defining_code")
    private NullFlavour kommentarNullFlavourDefiningCode;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/timing
     */
    @Path("/timing")
    private DvParsable timing;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/action_archetype_id
     */
    @Path("/action_archetype_id")
    private String actionArchetypeId;

    /**
     * Path: Medikamentenliste/Verordnung von Arzneimittel/Verordnung/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    public String getHandelsnameValue() {
        return this.handelsnameValue;
    }

    public void setHandelsnameValue(String handelsnameValue) {
        this.handelsnameValue = handelsnameValue;
    }

    public NullFlavour getHandelsnameNullFlavourDefiningCode() {
        return this.handelsnameNullFlavourDefiningCode;
    }

    public void setHandelsnameNullFlavourDefiningCode(
            NullFlavour handelsnameNullFlavourDefiningCode) {
        this.handelsnameNullFlavourDefiningCode = handelsnameNullFlavourDefiningCode;
    }

    public ArzneimittelCluster getArzneimittel() {
        return this.arzneimittel;
    }

    public void setArzneimittel(ArzneimittelCluster arzneimittel) {
        this.arzneimittel = arzneimittel;
    }

    public Cluster getLokalisation() {
        return this.lokalisation;
    }

    public void setLokalisation(Cluster lokalisation) {
        this.lokalisation = lokalisation;
    }

    public List<Cluster> getVerabreichungsmittel() {
        return this.verabreichungsmittel;
    }

    public void setVerabreichungsmittel(List<Cluster> verabreichungsmittel) {
        this.verabreichungsmittel = verabreichungsmittel;
    }

    public List<Cluster> getStrukturierteDosisUndZeitangaben() {
        return this.strukturierteDosisUndZeitangaben;
    }

    public void setStrukturierteDosisUndZeitangaben(List<Cluster> strukturierteDosisUndZeitangaben) {
        this.strukturierteDosisUndZeitangaben = strukturierteDosisUndZeitangaben;
    }

    public String getHinweiseZurEinnahmeValue() {
        return this.hinweiseZurEinnahmeValue;
    }

    public void setHinweiseZurEinnahmeValue(String hinweiseZurEinnahmeValue) {
        this.hinweiseZurEinnahmeValue = hinweiseZurEinnahmeValue;
    }

    public NullFlavour getHinweiseZurEinnahmeNullFlavourDefiningCode() {
        return this.hinweiseZurEinnahmeNullFlavourDefiningCode;
    }

    public void setHinweiseZurEinnahmeNullFlavourDefiningCode(
            NullFlavour hinweiseZurEinnahmeNullFlavourDefiningCode) {
        this.hinweiseZurEinnahmeNullFlavourDefiningCode = hinweiseZurEinnahmeNullFlavourDefiningCode;
    }

    public String getEinnahmegrundValue() {
        return this.einnahmegrundValue;
    }

    public void setEinnahmegrundValue(String einnahmegrundValue) {
        this.einnahmegrundValue = einnahmegrundValue;
    }

    public NullFlavour getEinnahmegrundNullFlavourDefiningCode() {
        return this.einnahmegrundNullFlavourDefiningCode;
    }

    public void setEinnahmegrundNullFlavourDefiningCode(
            NullFlavour einnahmegrundNullFlavourDefiningCode) {
        this.einnahmegrundNullFlavourDefiningCode = einnahmegrundNullFlavourDefiningCode;
    }

    public TemporalAccessor getDatumDerVerordnungValue() {
        return this.datumDerVerordnungValue;
    }

    public void setDatumDerVerordnungValue(TemporalAccessor datumDerVerordnungValue) {
        this.datumDerVerordnungValue = datumDerVerordnungValue;
    }

    public NullFlavour getDatumDerVerordnungNullFlavourDefiningCode() {
        return this.datumDerVerordnungNullFlavourDefiningCode;
    }

    public void setDatumDerVerordnungNullFlavourDefiningCode(
            NullFlavour datumDerVerordnungNullFlavourDefiningCode) {
        this.datumDerVerordnungNullFlavourDefiningCode = datumDerVerordnungNullFlavourDefiningCode;
    }

    public List<Cluster> getZusammenfassungDerVerordnung() {
        return this.zusammenfassungDerVerordnung;
    }

    public void setZusammenfassungDerVerordnung(List<Cluster> zusammenfassungDerVerordnung) {
        this.zusammenfassungDerVerordnung = zusammenfassungDerVerordnung;
    }

    public List<Cluster> getAnweisungenZurGenehmigung() {
        return this.anweisungenZurGenehmigung;
    }

    public void setAnweisungenZurGenehmigung(List<Cluster> anweisungenZurGenehmigung) {
        this.anweisungenZurGenehmigung = anweisungenZurGenehmigung;
    }

    public List<TherapeutischeAnweisungCluster> getTherapeutischeAnweisung() {
        return this.therapeutischeAnweisung;
    }

    public void setTherapeutischeAnweisung(
            List<TherapeutischeAnweisungCluster> therapeutischeAnweisung) {
        this.therapeutischeAnweisung = therapeutischeAnweisung;
    }

    public String getKommentarValue() {
        return this.kommentarValue;
    }

    public void setKommentarValue(String kommentarValue) {
        this.kommentarValue = kommentarValue;
    }

    public NullFlavour getKommentarNullFlavourDefiningCode() {
        return this.kommentarNullFlavourDefiningCode;
    }

    public void setKommentarNullFlavourDefiningCode(NullFlavour kommentarNullFlavourDefiningCode) {
        this.kommentarNullFlavourDefiningCode = kommentarNullFlavourDefiningCode;
    }

    public DvParsable getTiming() {
        return this.timing;
    }

    public void setTiming(DvParsable timing) {
        this.timing = timing;
    }

    public String getActionArchetypeId() {
        return this.actionArchetypeId;
    }

    public void setActionArchetypeId(String actionArchetypeId) {
        this.actionArchetypeId = actionArchetypeId;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }
}
