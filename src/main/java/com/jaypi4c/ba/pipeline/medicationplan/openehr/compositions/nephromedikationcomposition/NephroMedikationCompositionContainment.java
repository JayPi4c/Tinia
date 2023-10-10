package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition;

import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition.FallidentifikationCluster;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition.VerordnungVonArzneimittelInstruction;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListAqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListSelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Category;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;

import java.time.temporal.TemporalAccessor;

public class NephroMedikationCompositionContainment extends Containment {
    public SelectAqlField<NephroMedikationComposition> NEPHRO_MEDIKATION_COMPOSITION = new AqlFieldImp<NephroMedikationComposition>(NephroMedikationComposition.class, "", "NephroMedikationComposition", NephroMedikationComposition.class, this);

    public SelectAqlField<Category> CATEGORY_DEFINING_CODE = new AqlFieldImp<Category>(NephroMedikationComposition.class, "/category|defining_code", "categoryDefiningCode", Category.class, this);

    public SelectAqlField<FallidentifikationCluster> FALLIDENTIFIKATION = new AqlFieldImp<FallidentifikationCluster>(NephroMedikationComposition.class, "/context/other_context[at0005]/items[openEHR-EHR-CLUSTER.case_identification.v0]", "fallidentifikation", FallidentifikationCluster.class, this);

    public SelectAqlField<TemporalAccessor> START_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(NephroMedikationComposition.class, "/context/start_time|value", "startTimeValue", TemporalAccessor.class, this);

    public ListSelectAqlField<Participation> PARTICIPATIONS = new ListAqlFieldImp<Participation>(NephroMedikationComposition.class, "/context/participations", "participations", Participation.class, this);

    public SelectAqlField<TemporalAccessor> END_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(NephroMedikationComposition.class, "/context/end_time|value", "endTimeValue", TemporalAccessor.class, this);

    public SelectAqlField<String> LOCATION = new AqlFieldImp<String>(NephroMedikationComposition.class, "/context/location", "location", String.class, this);

    public SelectAqlField<PartyIdentified> HEALTH_CARE_FACILITY = new AqlFieldImp<PartyIdentified>(NephroMedikationComposition.class, "/context/health_care_facility", "healthCareFacility", PartyIdentified.class, this);

    public SelectAqlField<Setting> SETTING_DEFINING_CODE = new AqlFieldImp<Setting>(NephroMedikationComposition.class, "/context/setting|defining_code", "settingDefiningCode", Setting.class, this);

    public ListSelectAqlField<VerordnungVonArzneimittelInstruction> VERORDNUNG_VON_ARZNEIMITTEL = new ListAqlFieldImp<VerordnungVonArzneimittelInstruction>(NephroMedikationComposition.class, "/content[openEHR-EHR-INSTRUCTION.medication_order.v2]", "verordnungVonArzneimittel", VerordnungVonArzneimittelInstruction.class, this);

    public SelectAqlField<PartyProxy> COMPOSER = new AqlFieldImp<PartyProxy>(NephroMedikationComposition.class, "/composer", "composer", PartyProxy.class, this);

    public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(NephroMedikationComposition.class, "/language", "language", Language.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(NephroMedikationComposition.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    public SelectAqlField<Territory> TERRITORY = new AqlFieldImp<Territory>(NephroMedikationComposition.class, "/territory", "territory", Territory.class, this);

    private NephroMedikationCompositionContainment() {
        super("openEHR-EHR-COMPOSITION.medication_list.v1");
    }

    public static NephroMedikationCompositionContainment getInstance() {
        return new NephroMedikationCompositionContainment();
    }
}
