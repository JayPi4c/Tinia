package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition;

import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.ICompositionFactory;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition.*;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartySelf;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import static com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils.*;


/**
 * Factory class which will later be used to create compositions and send them to an EhrBase instance.
 * <br>
 * For now, it is just used for testing purposes.
 * <br>
 *
 * @see <a href="https://ckm.highmed.org/ckm/templates/1246.169.1019">Nephro_Medikation in CKM</a> for more information.
 */
@Slf4j
@Component
public class NephroMedikationCompositionFactory implements ICompositionFactory<NephroMedikationComposition> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public NephroMedikationComposition createComposition(String[][] medicationMatrix, String date) {
        NephroMedikationComposition composition = prepareComposition(new NephroMedikationComposition());

        // setting Fallidentifikation
        FallidentifikationCluster fallidentifikation = new FallidentifikationCluster();
        fallidentifikation.setFallKennungValue("Medikationsplan 1");
        composition.setFallidentifikation(fallidentifikation);

        String[] formDict = loadDictionary("/dictionaries/DarreichungsformAllowlist.txt");


        // jede Zeile in der Tabelle ist eine Verordnung. So kann eine ganze Tabelle in einer composition gespeichert werden.
        List<VerordnungVonArzneimittelInstruction> list = new ArrayList<>();

        for (int i = 1; i < medicationMatrix.length; i++) {
            String[] row = medicationMatrix[i];
            String wirkstoff = row[0].trim();
            String handelsname = row[1].trim();
            String staerke = row[2].trim();
            String form = row[3].trim();
            String einheit = row[8].trim();
            String hinweise = row[9].trim();
            String grund = row[10].trim();
            LDResult result = findClosestWord(form, formDict);
            form = result.closestWord();
            log.info("Found {} for {}", form, result.targetWord());

            VerordnungVonArzneimittelInstruction arzneimittel = prepareInstruction(new VerordnungVonArzneimittelInstruction());


            VerordnungVonArzneimittelVerordnungActivity verordnung = new VerordnungVonArzneimittelVerordnungActivity();
            verordnung.setHandelsnameValue(handelsname);


            ArzneimittelCluster arzneimittelCluster = new ArzneimittelCluster();

            ArzneimittelDarreichungsformElement arzneimittelDarreichungsformElement = new ArzneimittelDarreichungsformElement();
            arzneimittelDarreichungsformElement.setValue(form);
            arzneimittelCluster.setDarreichungsform(List.of(arzneimittelDarreichungsformElement));

            WirkstoffCluster wirkstoffCluster = new WirkstoffCluster();
            wirkstoffCluster.setWirkstoffValue(wirkstoff);
            arzneimittelCluster.setWirkstoff(wirkstoffCluster);

            verordnung.setArzneimittel(arzneimittelCluster);


            try {
                TemporalAccessor datumDerVerordnungValue = dateFormatter.parse(date);
                verordnung.setDatumDerVerordnungValue(datumDerVerordnungValue);
            } catch (DateTimeParseException e) {
                log.error("Error while parsing date", e);
            }

            arzneimittel.setVerordnung(List.of(verordnung));
            list.add(arzneimittel);
        }

        composition.setVerordnungVonArzneimittel(list);

        return composition;
    }


    private static VerordnungVonArzneimittelInstruction prepareInstruction(VerordnungVonArzneimittelInstruction instruction) {
        instruction.setLanguage(Language.DE);
        instruction.setNarrativeValue("Lorem ispum");
        instruction.setSubject(new PartySelf());
        return instruction;
    }

    /**
     * Prepare composition with default values that are mandatory but always the same.
     *
     * @param composition to prepare
     * @return prepared composition
     */
    private static NephroMedikationComposition prepareComposition(NephroMedikationComposition composition) {
        composition.setLanguage(Language.DE);
        PartyIdentified composer = new PartyIdentified();
        composer.setName("Max Mustermann");
        composition.setComposer(composer);
        composition.setTerritory(Territory.DE);

        composition.setStartTimeValue(LocalDateTime.now());
        composition.setEndTimeValue(LocalDateTime.now());
        composition.setSettingDefiningCode(Setting.HOME);

        // TODO Hardcoded Krankenhaus?
        PartyIdentified healthCareFacility = new PartyIdentified();
        healthCareFacility.setName("Krankenhaus");
        composition.setHealthCareFacility(healthCareFacility);

        return composition;
    }


}
