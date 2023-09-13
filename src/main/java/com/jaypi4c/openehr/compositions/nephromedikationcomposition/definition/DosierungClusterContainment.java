package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.Double;
import java.lang.String;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

public class DosierungClusterContainment extends Containment {
  public SelectAqlField<DosierungCluster> DOSIERUNG_CLUSTER = new AqlFieldImp<DosierungCluster>(DosierungCluster.class, "", "DosierungCluster", DosierungCluster.class, this);

  public SelectAqlField<Double> DOSISMENGE_MAGNITUDE = new AqlFieldImp<Double>(DosierungCluster.class, "/items[at0144]/value|magnitude", "dosismengeMagnitude", Double.class, this);

  public SelectAqlField<String> DOSISMENGE_UNITS = new AqlFieldImp<String>(DosierungCluster.class, "/items[at0144]/value|units", "dosismengeUnits", String.class, this);

  public SelectAqlField<NullFlavour> DOSISMENGE_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungCluster.class, "/items[at0144]/null_flavour|defining_code", "dosismengeNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<String> DOSISEINHEIT_VALUE = new AqlFieldImp<String>(DosierungCluster.class, "/items[at0145]/value|value", "dosiseinheitValue", String.class, this);

  public SelectAqlField<NullFlavour> DOSISEINHEIT_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungCluster.class, "/items[at0145]/null_flavour|defining_code", "dosiseinheitNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<String> DOSIERUNG_FREITEXT_VALUE = new AqlFieldImp<String>(DosierungCluster.class, "/items[at0178]/value|value", "dosierungFreitextValue", String.class, this);

  public SelectAqlField<NullFlavour> DOSIERUNG_FREITEXT_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungCluster.class, "/items[at0178]/null_flavour|defining_code", "dosierungFreitextNullFlavourDefiningCode", NullFlavour.class, this);

  public ListSelectAqlField<TaeglicheDosierungCluster> TAEGLICHE_DOSIERUNG = new ListAqlFieldImp<TaeglicheDosierungCluster>(DosierungCluster.class, "/items[openEHR-EHR-CLUSTER.timing_daily.v1]", "taeglicheDosierung", TaeglicheDosierungCluster.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(DosierungCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  private DosierungClusterContainment() {
    super("openEHR-EHR-CLUSTER.dosage.v1");
  }

  public static DosierungClusterContainment getInstance() {
    return new DosierungClusterContainment();
  }
}
