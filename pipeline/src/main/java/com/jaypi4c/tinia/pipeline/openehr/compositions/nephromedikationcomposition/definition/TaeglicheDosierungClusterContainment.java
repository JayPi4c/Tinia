package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListAqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListSelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

public class TaeglicheDosierungClusterContainment extends Containment {
    public SelectAqlField<TaeglicheDosierungCluster> TAEGLICHE_DOSIERUNG_CLUSTER = new AqlFieldImp<TaeglicheDosierungCluster>(TaeglicheDosierungCluster.class, "", "TaeglicheDosierungCluster", TaeglicheDosierungCluster.class, this);

    public SelectAqlField<Boolean> BEI_BEDARF_VALUE = new AqlFieldImp<Boolean>(TaeglicheDosierungCluster.class, "/items[at0024]/value|value", "beiBedarfValue", Boolean.class, this);

    public SelectAqlField<NullFlavour> BEI_BEDARF_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(TaeglicheDosierungCluster.class, "/items[at0024]/null_flavour|defining_code", "beiBedarfNullFlavourDefiningCode", NullFlavour.class, this);

    public ListSelectAqlField<TaeglicheDosierungBestimmtesEreignisCluster> BESTIMMTES_EREIGNIS = new ListAqlFieldImp<TaeglicheDosierungBestimmtesEreignisCluster>(TaeglicheDosierungCluster.class, "/items[at0039]", "bestimmtesEreignis", TaeglicheDosierungBestimmtesEreignisCluster.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(TaeglicheDosierungCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    private TaeglicheDosierungClusterContainment() {
        super("openEHR-EHR-CLUSTER.timing_daily.v1");
    }

    public static TaeglicheDosierungClusterContainment getInstance() {
        return new TaeglicheDosierungClusterContainment();
    }
}
