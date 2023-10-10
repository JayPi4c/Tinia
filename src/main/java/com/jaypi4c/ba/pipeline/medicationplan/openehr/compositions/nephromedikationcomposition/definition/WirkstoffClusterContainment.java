package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListAqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListSelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

public class WirkstoffClusterContainment extends Containment {
    public SelectAqlField<WirkstoffCluster> WIRKSTOFF_CLUSTER = new AqlFieldImp<WirkstoffCluster>(WirkstoffCluster.class, "", "WirkstoffCluster", WirkstoffCluster.class, this);

    public SelectAqlField<String> WIRKSTOFF_VALUE = new AqlFieldImp<String>(WirkstoffCluster.class, "/items[at0132]/value|value", "wirkstoffValue", String.class, this);

    public SelectAqlField<NullFlavour> WIRKSTOFF_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(WirkstoffCluster.class, "/items[at0132]/null_flavour|defining_code", "wirkstoffNullFlavourDefiningCode", NullFlavour.class, this);

    public SelectAqlField<Double> WIRKSTOFFSTAERKE_MAGNITUDE = new AqlFieldImp<Double>(WirkstoffCluster.class, "/items[at0115]/value|magnitude", "wirkstoffstaerkeMagnitude", Double.class, this);

    public SelectAqlField<String> WIRKSTOFFSTAERKE_UNITS = new AqlFieldImp<String>(WirkstoffCluster.class, "/items[at0115]/value|units", "wirkstoffstaerkeUnits", String.class, this);

    public SelectAqlField<NullFlavour> WIRKSTOFFSTAERKE_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(WirkstoffCluster.class, "/items[at0115]/null_flavour|defining_code", "wirkstoffstaerkeNullFlavourDefiningCode", NullFlavour.class, this);

    public ListSelectAqlField<Cluster> BESTANDTEIL = new ListAqlFieldImp<Cluster>(WirkstoffCluster.class, "/items[at0138]", "bestandteil", Cluster.class, this);

    public ListSelectAqlField<Cluster> STRUKTURIERTE_ANGABEN = new ListAqlFieldImp<Cluster>(WirkstoffCluster.class, "/items[at0141]", "strukturierteAngaben", Cluster.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(WirkstoffCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    private WirkstoffClusterContainment() {
        super("openEHR-EHR-CLUSTER.medication.v1");
    }

    public static WirkstoffClusterContainment getInstance() {
        return new WirkstoffClusterContainment();
    }
}
