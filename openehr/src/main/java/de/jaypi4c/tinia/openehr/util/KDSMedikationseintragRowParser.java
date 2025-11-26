package de.jaypi4c.tinia.openehr.util;

import com.nedap.archie.rm.generic.PartyIdentified;
import de.jaypi4c.tinia.openehr.entities.kdsmedikationseintragcomposition.definition.*;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Component
public class KDSMedikationseintragRowParser extends AbstractRowParser<MedikationseintragObservation> {

    @Override
    public MedikationseintragObservation parseRow(String[] rowData) {
        Row row = parse(rowData);
        if (row.dosageSchema().isCombinedDosageSchema())
            return mapCombinedDosageSchema(row);
        else
            return mapStandardDosageSchema(row);
    }

    private MedikationseintragObservation mapStandardDosageSchema(Row row) {
        MedikationseintragObservation observation = map(row);
        // TODO dosage
        return observation;
    }

    private MedikationseintragObservation mapCombinedDosageSchema(Row row) {
        MedikationseintragObservation observation = map(row);
        // TODO dosage
        return observation;
    }


    private MedikationseintragObservation map(Row row) {
        MedikationseintragObservation observation = new MedikationseintragObservation();
        ArzneimittelCluster arzneimittelCluster = new ArzneimittelCluster();
        // active ingredient
        WirkstoffCluster wirkstoffCluster = new WirkstoffCluster();
        wirkstoffCluster.setWirkstoffNameValue(row.activeIngredient());
        WirkstoffCodeAskSnomedCtUniiCasCluster wirkstoffCodeAskSnomedCtUniiCasCluster = new WirkstoffCodeAskSnomedCtUniiCasCluster();
        // FIXME: determine wirkstoff Code
        wirkstoffCodeAskSnomedCtUniiCasCluster.setWirkstoffCodeValue("tbd");
        wirkstoffCluster.setWirkstoffCodeAskSnomedCtUniiCas(List.of(wirkstoffCodeAskSnomedCtUniiCasCluster));
        // name
        arzneimittelCluster.setArzneimittelNameValue(row.name());
        // strength
        parseStaerke(row.strength()).ifPresent(magnitudeUnitPair -> {
            arzneimittelCluster.setWirkstaerkeKonzentrationMagnitude(magnitudeUnitPair.magnitude());
            arzneimittelCluster.setWirkstaerkeKonzentrationUnits(magnitudeUnitPair.unit());
        });
        // form
        arzneimittelCluster.setDarreichungsformDefiningCode(mapDarreichungsform(row.form()));
        arzneimittelCluster.setWirkstoff(List.of(wirkstoffCluster));
        observation.setArzneimittel(arzneimittelCluster);

        // TODO Unit?

        // hint
        observation.setHinweisValue(row.hint());
        // reason
        observation.setBehandlungsgrundValue(row.reason());


        // required fields
        observation.setStartzeitpunktEinnahmeValue(LocalDateTime.now()); // TODO use date from source
        observation.setDauerDerEinnahmeValue(Period.ofYears(0)); // zero period means unspecified duration // TODO adjust to correct value
        observation.setLanguage(Language.DE);
        StatusCluster statusCluster = new StatusCluster();
        statusCluster.setStatusDefiningCode(StatusDefiningCode.UNBEKANNT);
        observation.setStatus(statusCluster);


        // TODO check if values are meaningful
        observation.setOriginValue(LocalDateTime.now());
        observation.setTimeValue(LocalDateTime.now());
        PartyIdentified subject = new PartyIdentified();
        subject.setName("Tinia (automated)");
        observation.setSubject(subject);
        return observation;
    }

    private DarreichungsformDefiningCode mapDarreichungsform(String form) {
        // FIXME: implement mapping
        return DarreichungsformDefiningCode.TABL;
    }

}
