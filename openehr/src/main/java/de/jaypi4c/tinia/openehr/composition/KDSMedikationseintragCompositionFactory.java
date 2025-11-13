package de.jaypi4c.tinia.openehr.composition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import de.jaypi4c.tinia.openehr.entities.kdsmedikationseintragcomposition.KDSMedikationseintragComposition;
import de.jaypi4c.tinia.openehr.entities.kdsmedikationseintragcomposition.definition.FallidentifikationCluster;
import de.jaypi4c.tinia.openehr.entities.kdsmedikationseintragcomposition.definition.MedikationseintragObservation;
import de.jaypi4c.tinia.openehr.util.RowParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KDSMedikationseintragCompositionFactory implements CompositionFactory<KDSMedikationseintragComposition> {

    private final RowParser<MedikationseintragObservation> rowParser;

    @Override
    public KDSMedikationseintragComposition createComposition(String[][] medicationMatrix, String date, String metadata) {
        KDSMedikationseintragComposition composition = prepareComposition(new KDSMedikationseintragComposition());

        FeederAudit feederAudit = createFeederAudit(metadata);
        composition.setFeederAudit(feederAudit);

        // TODO: find better way to create Fallidentifikation and maybe extract also into interface
        FallidentifikationCluster fallidentifikation = new FallidentifikationCluster();
        fallidentifikation.setFallKennungValue(getCaseID());
        composition.setFallidentifikation(fallidentifikation);

        List<MedikationseintragObservation> medikationseintragList = new ArrayList<>();
        // start with one to skip header
        for (int i = 1; i < medicationMatrix.length; i++) {
            String[] row = medicationMatrix[i];
            medikationseintragList.add(rowParser.parseRow(row));
        }
        composition.setMedikationseintrag(medikationseintragList);

        return composition;
    }

    @Override
    public String getTemplateId() {
        return "KDS_Medikationseintrag";
    }
}
