package com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.lang.String;
import java.time.temporal.TemporalAmount;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

public class DosierungNichtTaeglichClusterContainment extends Containment {
  public SelectAqlField<DosierungNichtTaeglichCluster> DOSIERUNG_NICHT_TAEGLICH_CLUSTER = new AqlFieldImp<DosierungNichtTaeglichCluster>(DosierungNichtTaeglichCluster.class, "", "DosierungNichtTaeglichCluster", DosierungNichtTaeglichCluster.class, this);

  public SelectAqlField<TemporalAmount> WIEDERHOLUNGSINTERVALL_VALUE = new AqlFieldImp<TemporalAmount>(DosierungNichtTaeglichCluster.class, "/items[at0002]/value|value", "wiederholungsintervallValue", TemporalAmount.class, this);

  public SelectAqlField<NullFlavour> WIEDERHOLUNGSINTERVALL_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungNichtTaeglichCluster.class, "/items[at0002]/null_flavour|defining_code", "wiederholungsintervallNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<NullFlavour> FREQUENZ_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungNichtTaeglichCluster.class, "/items[at0014]/null_flavour|defining_code", "frequenzNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<BestimmterWochentagDefiningCode> BESTIMMTER_WOCHENTAG_DEFINING_CODE = new AqlFieldImp<BestimmterWochentagDefiningCode>(DosierungNichtTaeglichCluster.class, "/items[at0003]/value|defining_code", "bestimmterWochentagDefiningCode", BestimmterWochentagDefiningCode.class, this);

  public SelectAqlField<NullFlavour> BESTIMMTER_WOCHENTAG_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungNichtTaeglichCluster.class, "/items[at0003]/null_flavour|defining_code", "bestimmterWochentagNullFlavourDefiningCode", NullFlavour.class, this);

  public SelectAqlField<String> BESCHREIBUNG_DES_ZEITABLAUFS_VALUE = new AqlFieldImp<String>(DosierungNichtTaeglichCluster.class, "/items[at0021]/value|value", "beschreibungDesZeitablaufsValue", String.class, this);

  public SelectAqlField<NullFlavour> BESCHREIBUNG_DES_ZEITABLAUFS_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(DosierungNichtTaeglichCluster.class, "/items[at0021]/null_flavour|defining_code", "beschreibungDesZeitablaufsNullFlavourDefiningCode", NullFlavour.class, this);

  public ListSelectAqlField<DosierungNichtTaeglichBestimmtesEreignisCluster> BESTIMMTES_EREIGNIS = new ListAqlFieldImp<DosierungNichtTaeglichBestimmtesEreignisCluster>(DosierungNichtTaeglichCluster.class, "/items[at0006]", "bestimmtesEreignis", DosierungNichtTaeglichBestimmtesEreignisCluster.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(DosierungNichtTaeglichCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  public SelectAqlField<DosierungNichtTaeglichFrequenzChoice> FREQUENZ = new AqlFieldImp<DosierungNichtTaeglichFrequenzChoice>(DosierungNichtTaeglichCluster.class, "/items[at0014]/value", "frequenz", DosierungNichtTaeglichFrequenzChoice.class, this);

  private DosierungNichtTaeglichClusterContainment() {
    super("openEHR-EHR-CLUSTER.timing_nondaily.v1");
  }

  public static DosierungNichtTaeglichClusterContainment getInstance() {
    return new DosierungNichtTaeglichClusterContainment();
  }
}
