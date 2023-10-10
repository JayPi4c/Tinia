package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.PartyProxy;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListAqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListSelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;

import java.time.temporal.TemporalAccessor;

public class VerordnungVonArzneimittelInstructionContainment extends Containment {
    public SelectAqlField<VerordnungVonArzneimittelInstruction> VERORDNUNG_VON_ARZNEIMITTEL_INSTRUCTION = new AqlFieldImp<VerordnungVonArzneimittelInstruction>(VerordnungVonArzneimittelInstruction.class, "", "VerordnungVonArzneimittelInstruction", VerordnungVonArzneimittelInstruction.class, this);

    public ListSelectAqlField<VerordnungVonArzneimittelVerordnungActivity> VERORDNUNG = new ListAqlFieldImp<VerordnungVonArzneimittelVerordnungActivity>(VerordnungVonArzneimittelInstruction.class, "/activities[at0001]", "verordnung", VerordnungVonArzneimittelVerordnungActivity.class, this);

    public SelectAqlField<AutorEintragCluster> AUTOR_EINTRAG = new AqlFieldImp<AutorEintragCluster>(VerordnungVonArzneimittelInstruction.class, "/protocol[at0005]/items[openEHR-EHR-CLUSTER.organisation.v0]", "autorEintrag", AutorEintragCluster.class, this);

    public SelectAqlField<PartyProxy> SUBJECT = new AqlFieldImp<PartyProxy>(VerordnungVonArzneimittelInstruction.class, "/subject", "subject", PartyProxy.class, this);

    public SelectAqlField<String> NARRATIVE_VALUE = new AqlFieldImp<String>(VerordnungVonArzneimittelInstruction.class, "/narrative|value", "narrativeValue", String.class, this);

    public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(VerordnungVonArzneimittelInstruction.class, "/language", "language", Language.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(VerordnungVonArzneimittelInstruction.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    public SelectAqlField<TemporalAccessor> EXPIRY_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(VerordnungVonArzneimittelInstruction.class, "/expiry_time|value", "expiryTimeValue", TemporalAccessor.class, this);

    private VerordnungVonArzneimittelInstructionContainment() {
        super("openEHR-EHR-INSTRUCTION.medication_order.v2");
    }

    public static VerordnungVonArzneimittelInstructionContainment getInstance() {
        return new VerordnungVonArzneimittelInstructionContainment();
    }
}
