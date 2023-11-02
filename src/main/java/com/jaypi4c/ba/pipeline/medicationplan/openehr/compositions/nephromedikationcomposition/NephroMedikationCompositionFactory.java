package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition;

import com.google.gson.Gson;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.ICompositionFactory;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition.*;
import com.jaypi4c.ba.pipeline.medicationplan.utils.DarreichungsformHelper;
import com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils;
import com.jaypi4c.ba.pipeline.medicationplan.validation.IActiveIngredientValidator;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.archetyped.FeederAuditDetails;
import com.nedap.archie.rm.datavalues.encapsulated.DvEncapsulated;
import com.nedap.archie.rm.datavalues.encapsulated.DvParsable;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartySelf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;

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
@RequiredArgsConstructor
public class NephroMedikationCompositionFactory implements ICompositionFactory<NephroMedikationComposition> {

    @Value("${validation.active}")
    private boolean validationActive;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final IActiveIngredientValidator validator;

    private final DarreichungsformHelper darreichungsformHelper;

    @Override
    public NephroMedikationComposition createComposition(String[][] medicationMatrix, String date, String metadataJson) {
        NephroMedikationComposition composition = prepareComposition(new NephroMedikationComposition());

        FeederAudit feederAudit = createFeederAudit(metadataJson);
        composition.setFeederAudit(feederAudit);

        // setting Fallidentifikation
        FallidentifikationCluster fallidentifikation = new FallidentifikationCluster();
        fallidentifikation.setFallKennungValue("Medikationsplan 1");
        composition.setFallidentifikation(fallidentifikation);

        String[] formDict = loadDictionary("/dictionaries/DarreichungsformAllowlist.txt");
        Map<String, String> aliases = loadAliases("/aliases/alias.json");


        // jede Zeile in der Tabelle ist eine Verordnung. So kann eine ganze Tabelle in einer composition gespeichert werden.
        List<VerordnungVonArzneimittelInstruction> list = new ArrayList<>();

        for (int i = 1; i < medicationMatrix.length; i++) {
            String[] row = medicationMatrix[i];
            String wirkstoff = row[0].trim();
            if (validationActive) {
                if (validator.validate(wirkstoff)) {
                    log.info("Validated {}", wirkstoff);
                } else {
                    log.warn("Could not validate {}", wirkstoff);
                }
            } else {
                log.info("Validation deactivated");
            }

            String handelsname = row[1].trim();
            String staerke = row[2].trim();
            String form = row[3].trim();
            String einheit = row[8].trim();
            String hinweise = row[9].trim();
            String grund = row[10].trim();


            Optional<String> newForm = darreichungsformHelper.getBezeichnungIFAByV(form);
            if (newForm.isEmpty()) {
                newForm = darreichungsformHelper.getBezeichnungIFAByDN(form);
                if (newForm.isEmpty()) {
                    log.warn("Could not find form {} in dictionary", form);
                } else {
                    log.info("Found {} for {}", newForm.get(), form);
                }
            } else {
                log.info("Found {} for {}", newForm.get(), form);
            }

            if (aliases.containsKey(form)) {
                String oldForm = form;
                form = aliases.get(form);
                log.info("Changed {} to {} via alias ", oldForm, form);
            } else {
                LDResult result = findClosestWord(form, formDict);
                form = result.closestWord();
                log.info("Found {} for {}", form, result.targetWord());
            }

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
            } catch (DateTimeParseException | NullPointerException e) {
                log.error("Error while parsing date; Maybe it's blacked out or not found in the pdf");
            }

            arzneimittel.setVerordnung(List.of(verordnung));
            list.add(arzneimittel);
        }

        composition.setVerordnungVonArzneimittel(list);

        return composition;
    }


    private static VerordnungVonArzneimittelInstruction prepareInstruction(VerordnungVonArzneimittelInstruction
                                                                                   instruction) {
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
        composer.setName("Medication Plan Pipeline (automated)");
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

    /**
     * Create FeederAudit object with metadata.
     *
     * @param metadataJson json string with pdf metadata
     * @return FeederAudit object
     * @see <a href="https://specifications.openehr.org/releases/RM/latest/data_types.html#_examples">OpenEHR Examples</a>
     */
    private static FeederAudit createFeederAudit(String metadataJson) {
        FeederAudit feederAudit = new FeederAudit();
        FeederAuditDetails feederAuditDetails = new FeederAuditDetails();
        feederAuditDetails.setSystemId("Medication Plan Pipeline (automated)");
        feederAudit.setOriginatingSystemAudit(feederAuditDetails);
        DvEncapsulated originalContent = new DvParsable(metadataJson, "json");
        feederAudit.setOriginalContent(originalContent);

        return feederAudit;
    }

    public static Map<String, String> loadAliases(String path) {
        try (InputStream is = WordUtils.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            Gson gson = new Gson();
            Aliases[] aliases = gson.fromJson(json.toString(), Aliases[].class);
            Map<String, String> map = new HashMap<>();
            for (Aliases a : aliases) {
                map.putAll(a.toMap());
            }
            return map;
        } catch (Exception e) {
            log.error("Error while loading aliases", e);
        }
        return new HashMap<>();
    }

    private record Aliases(String real, String[] aliases) {
        Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            for (String alias : aliases) {
                map.put(alias, real);
            }
            return map;
        }
    }
}
