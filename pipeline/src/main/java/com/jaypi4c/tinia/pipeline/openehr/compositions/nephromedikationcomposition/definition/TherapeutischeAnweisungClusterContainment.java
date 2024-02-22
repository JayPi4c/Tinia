package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListAqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.ListSelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;

public class TherapeutischeAnweisungClusterContainment extends Containment {
    public SelectAqlField<TherapeutischeAnweisungCluster> THERAPEUTISCHE_ANWEISUNG_CLUSTER = new AqlFieldImp<TherapeutischeAnweisungCluster>(TherapeutischeAnweisungCluster.class, "", "TherapeutischeAnweisungCluster", TherapeutischeAnweisungCluster.class, this);

    public ListSelectAqlField<DosierungCluster> DOSIERUNG = new ListAqlFieldImp<DosierungCluster>(TherapeutischeAnweisungCluster.class, "/items[openEHR-EHR-CLUSTER.dosage.v1]", "dosierung", DosierungCluster.class, this);

    public SelectAqlField<DosierungNichtTaeglichCluster> DOSIERUNG_NICHT_TAEGLICH = new AqlFieldImp<DosierungNichtTaeglichCluster>(TherapeutischeAnweisungCluster.class, "/items[openEHR-EHR-CLUSTER.timing_nondaily.v1]", "dosierungNichtTaeglich", DosierungNichtTaeglichCluster.class, this);

    public ListSelectAqlField<Cluster> ZUSAETZLICHE_ANGABEN = new ListAqlFieldImp<Cluster>(TherapeutischeAnweisungCluster.class, "/items[at0156]", "zusaetzlicheAngaben", Cluster.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(TherapeutischeAnweisungCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    private TherapeutischeAnweisungClusterContainment() {
        super("openEHR-EHR-CLUSTER.therapeutic_direction.v1");
    }

    public static TherapeutischeAnweisungClusterContainment getInstance() {
        return new TherapeutischeAnweisungClusterContainment();
    }
}
