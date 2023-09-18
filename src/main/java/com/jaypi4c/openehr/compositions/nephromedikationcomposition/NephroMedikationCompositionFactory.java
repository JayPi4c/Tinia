package com.jaypi4c.openehr.compositions.nephromedikationcomposition;

import com.jaypi4c.openehr.compositions.ICompositionFactory;
import com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition.*;

import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartySelf;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Factory class which will later be used to create compositions and send them to an EhrBase instance.
 * <br>
 * For now, it is just used for testing purposes.
 * <br>
 *
 * @see <a href="https://ckm.highmed.org/ckm/templates/1246.169.1019">Nephro_Medikation in CKM</a> for more information.
 */
@Slf4j
public class NephroMedikationCompositionFactory implements ICompositionFactory<NephroMedikationComposition> {

    public NephroMedikationComposition createComposition(String[][] medicationMatrix) {
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
            LDResult result = findClosestWord(form, formDict);
            form = result.closestWord();
            log.info("Found {} for {}", form, result.targetWord());


            // TODO: extract information from matrix and put into composition
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


            arzneimittel.setVerordnung(List.of(verordnung));
            list.add(arzneimittel);
        }

        composition.setVerordnungVonArzneimittel(list);

        return composition;
    }

    private LDResult findClosestWord(String word, String[] dict) {
        final LevenshteinDistance ld = LevenshteinDistance.getDefaultInstance();

        int minDistance = Integer.MAX_VALUE;
        String closestWord = "";

        for (String dictWord : dict) {
            int distance = ld.apply(word, dictWord);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = dictWord;
                if (minDistance == 0) // we found exact match
                    break;
            }
        }
        return new LDResult(minDistance, closestWord, word);
    }

    private String[] loadDictionary(String path) {
        List<String> lines = new ArrayList<>();

        try (InputStream is = NephroMedikationCompositionFactory.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.error("Failed to load dictionary", e);
        }
        return lines.toArray(new String[0]);
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

    public record LDResult(int distance, String closestWord, String targetWord) {
        public boolean exactMatch() {
            return distance == 0;
        }
    }


}
