package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import org.ehrbase.openehr.sdk.generator.commons.aql.containment.Containment;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.AqlFieldImp;
import org.ehrbase.openehr.sdk.generator.commons.aql.field.SelectAqlField;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.NullFlavour;

public class AutorEintragClusterContainment extends Containment {
    public SelectAqlField<AutorEintragCluster> AUTOR_EINTRAG_CLUSTER = new AqlFieldImp<AutorEintragCluster>(AutorEintragCluster.class, "", "AutorEintragCluster", AutorEintragCluster.class, this);

    public SelectAqlField<String> EINRICHTUNG_ORGANISATION_VALUE = new AqlFieldImp<String>(AutorEintragCluster.class, "/items[at0001]/value|value", "einrichtungOrganisationValue", String.class, this);

    public SelectAqlField<NullFlavour> EINRICHTUNG_ORGANISATION_NULL_FLAVOUR_DEFINING_CODE = new AqlFieldImp<NullFlavour>(AutorEintragCluster.class, "/items[at0001]/null_flavour|defining_code", "einrichtungOrganisationNullFlavourDefiningCode", NullFlavour.class, this);

    public SelectAqlField<Cluster> ADRESSANGABEN = new AqlFieldImp<Cluster>(AutorEintragCluster.class, "/items[at0008]", "adressangaben", Cluster.class, this);

    public SelectAqlField<Cluster> TELEKOMMUNIKATIONSDETAILS = new AqlFieldImp<Cluster>(AutorEintragCluster.class, "/items[at0009]", "telekommunikationsdetails", Cluster.class, this);

    public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(AutorEintragCluster.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

    private AutorEintragClusterContainment() {
        super("openEHR-EHR-CLUSTER.organisation.v0");
    }

    public static AutorEintragClusterContainment getInstance() {
        return new AutorEintragClusterContainment();
    }
}
