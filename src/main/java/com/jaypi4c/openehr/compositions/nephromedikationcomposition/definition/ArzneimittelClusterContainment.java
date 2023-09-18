package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;

public class ArzneimittelClusterContainment extends Containment {
  public SelectAqlField<ArzneimittelCluster> ARZNEIMITTEL_CLUSTER = new AqlFieldImp<ArzneimittelCluster>(ArzneimittelCluster.class, "", "ArzneimittelCluster", ArzneimittelCluster.class, this);

  public ListSelectAqlField<ArzneimittelDarreichungsformElement> DARREICHUNGSFORM = new ListAqlFieldImp<ArzneimittelDarreichungsformElement>(ArzneimittelCluster.class, "/items[at0071]", "darreichungsform", ArzneimittelDarreichungsformElement.class, this);

  public SelectAqlField<WirkstoffCluster> WIRKSTOFF = new AqlFieldImp<WirkstoffCluster>(ArzneimittelCluster.class, "/items[openEHR-EHR-CLUSTER.medication.v1]", "wirkstoff", WirkstoffCluster.class, this);

  public ListSelectAqlField<Cluster> STRUKTURIERTE_ANGABEN = new ListAqlFieldImp<Cluster>(ArzneimittelCluster.class, "/items[at0141]", "strukturierteAngaben", Cluster.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(ArzneimittelCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  private ArzneimittelClusterContainment() {
    super("openEHR-EHR-CLUSTER.medication.v1");
  }

  public static ArzneimittelClusterContainment getInstance() {
    return new ArzneimittelClusterContainment();
  }
}
