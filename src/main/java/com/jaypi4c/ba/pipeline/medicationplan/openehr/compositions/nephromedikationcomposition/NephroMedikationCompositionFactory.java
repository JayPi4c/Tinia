package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition;

import com.google.gson.Gson;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.ICompositionFactory;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.definition.*;
import com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils;
import com.jaypi4c.ba.pipeline.medicationplan.validation.IActiveIngredientValidator;
import com.jaypi4c.ba.pipeline.medicationplan.validation.helpers.DarreichungsformHelper;
import com.jaypi4c.ba.pipeline.medicationplan.validation.helpers.EinheitenHelper;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.archetyped.FeederAuditDetails;
import com.nedap.archie.rm.datavalues.encapsulated.DvEncapsulated;
import com.nedap.archie.rm.datavalues.encapsulated.DvParsable;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartySelf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils.LDResult;
import static com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils.findClosestWord;


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

    @Value("${validation.active}")
    private boolean validationActive;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final IActiveIngredientValidator validator;

    private final String[] formDict;
    private final Map<String, String> formAliases;

    private final String[] einheitDict;

    private static final Logger validationLogger = LoggerFactory.getLogger("VALIDATION-LOGGER");

    @Autowired
    public NephroMedikationCompositionFactory(IActiveIngredientValidator validator, DarreichungsformHelper darreichungsformHelper, EinheitenHelper einheitenHelper) {
        this.validator = validator;
        formDict = darreichungsformHelper.getDictionary(); //loadDictionary("/dictionaries/DarreichungsformAllowlist.txt");
        formAliases = darreichungsformHelper.getAliases();//loadAliases("/aliases/alias.json");
        einheitDict = einheitenHelper.getDictionary();
    }


    @Override
    public NephroMedikationComposition createComposition(String[][] medicationMatrix, String date, String metadataJson) {
        NephroMedikationComposition composition = prepareComposition(new NephroMedikationComposition());

        FeederAudit feederAudit = createFeederAudit(metadataJson);
        composition.setFeederAudit(feederAudit);

        // setting Fallidentifikation
        FallidentifikationCluster fallidentifikation = new FallidentifikationCluster();
        fallidentifikation.setFallKennungValue("Medikationsplan 1");
        composition.setFallidentifikation(fallidentifikation);


        // jede Zeile in der Tabelle ist eine Verordnung. So kann eine ganze Tabelle in einer composition gespeichert werden.
        List<VerordnungVonArzneimittelInstruction> list = new ArrayList<>();

        for (int i = 1; i < medicationMatrix.length; i++) {
            String[] row = medicationMatrix[i];
            String wirkstoff = row[0].trim();
            if (validationActive) {
                if (validator.validate(wirkstoff)) {
                    validationLogger.info("[WIRKSTOFF]: Validated {}", wirkstoff);
                    log.info("[WIRKSTOFF]: Validated {}", wirkstoff);
                } else {
                    validationLogger.warn("[WIRKSTOFF]: Could not validate {}", wirkstoff);
                    log.warn("[WIRKSTOFF]: Could not validate {}", wirkstoff);
                }
            } else {
                validationLogger.info("[WIRKSTOFF]: Validation deactivated");
                log.info("[WIRKSTOFF]: Validation deactivated");
            }

            String handelsname = row[1].trim();
            String staerke = row[2].trim();
            String form = row[3].trim();
            String morgens = row[4].trim();
            String mittags = row[5].trim();
            String abends = row[6].trim();
            String nacht = row[7].trim();
            String einheit = row[8].trim();
            String hinweise = row[9].trim();
            String grund = row[10].trim();


            form = checkForm(form);

            einheit = checkEinheit(einheit);

            VerordnungVonArzneimittelInstruction arzneimittel = prepareInstruction(new VerordnungVonArzneimittelInstruction());


            VerordnungVonArzneimittelVerordnungActivity verordnung = new VerordnungVonArzneimittelVerordnungActivity();
            verordnung.setHandelsnameValue(handelsname);
            verordnung.setHinweiseZurEinnahmeValue(hinweise);
            verordnung.setEinnahmegrundValue(grund);
            verordnung.setKommentarValue("Entry generated by Medication Plan Pipeline (automated)");


            ArzneimittelCluster arzneimittelCluster = getArzneimittelCluster(form, wirkstoff, staerke);
            verordnung.setArzneimittel(arzneimittelCluster);

            TherapeutischeAnweisungCluster therapeutischeAnweisungCluster = new TherapeutischeAnweisungCluster();
            List<DosierungCluster> dosierungen = new ArrayList<>();
            // morgens
            if (!morgens.isBlank()) {
                double dosismenge = parseDosisMenge(morgens);
                DosierungCluster dosierungCluster = getDosierungCluster(dosismenge, einheit, "Lorem Ipsum", "morgens");
                dosierungen.add(dosierungCluster);
            }
            if (!mittags.isBlank()) {
                double dosismenge = parseDosisMenge(mittags);
                DosierungCluster dosierungCluster = getDosierungCluster(dosismenge, einheit, "Lorem Ipsum", "mittags");
                dosierungen.add(dosierungCluster);
            }
            if (!abends.isBlank()) {
                double dosismenge = parseDosisMenge(abends);
                DosierungCluster dosierungCluster = getDosierungCluster(dosismenge, einheit, "Lorem Ipsum", "abends");
                dosierungen.add(dosierungCluster);
            }
            if (!nacht.isBlank()) {
                double dosismenge = parseDosisMenge(nacht);
                DosierungCluster dosierungCluster = getDosierungCluster(dosismenge, einheit, "Lorem Ipsum", "nachts");
                dosierungen.add(dosierungCluster);
            }
            therapeutischeAnweisungCluster.setDosierung(dosierungen);
            verordnung.setTherapeutischeAnweisung(List.of(therapeutischeAnweisungCluster));


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

    //https://stackoverflow.com/a/10372905/13670629
    private static double parseDosisMenge(String dosisMenge) {
        String s = dosisMenge.replaceAll("[^\\d.,]", "");
        s = s.replace(",", ".");
        if (s.isEmpty())
            return 0;
        return Double.parseDouble(s);
    }

    private static DosierungCluster getDosierungCluster(double dosismenge, String dosisEinheit, String dosierungFreitext, String ereignis) {
        DosierungCluster dosierungCluster = new DosierungCluster();
        dosierungCluster.setDosismengeMagnitude(dosismenge);
        dosierungCluster.setDosismengeUnits("1");// dosisEinheit);
        dosierungCluster.setDosiseinheitValue(dosisEinheit);
        dosierungCluster.setDosierungFreitextValue(dosierungFreitext);

        TaeglicheDosierungBestimmtesEreignisCluster taeglicheDosierungBestimmtesEreignisCluster = new TaeglicheDosierungBestimmtesEreignisCluster();
        taeglicheDosierungBestimmtesEreignisCluster.setEreignisValue(ereignis);


        TaeglicheDosierungCluster taeglicheDosierungCluster = new TaeglicheDosierungCluster();
        taeglicheDosierungCluster.setBestimmtesEreignis(List.of(taeglicheDosierungBestimmtesEreignisCluster));
        // taeglicheDosierungCluster.setBeiBedarfValue(false);

        dosierungCluster.setTaeglicheDosierung(List.of(taeglicheDosierungCluster));
        return dosierungCluster;
    }

    private static ArzneimittelCluster getArzneimittelCluster(String form, String wirkstoff, String staerke) {
        ArzneimittelCluster arzneimittelCluster = new ArzneimittelCluster();

        ArzneimittelDarreichungsformElement arzneimittelDarreichungsformElement = new ArzneimittelDarreichungsformElement();
        arzneimittelDarreichungsformElement.setValue(form);
        arzneimittelCluster.setDarreichungsform(List.of(arzneimittelDarreichungsformElement));

        WirkstoffCluster wirkstoffCluster = new WirkstoffCluster();
        wirkstoffCluster.setWirkstoffValue(wirkstoff);

        var starkePair = parseStaerke(staerke);
        starkePair.ifPresent(pair -> {
            wirkstoffCluster.setWirkstoffstaerkeMagnitude(pair.getLeft());
            wirkstoffCluster.setWirkstoffstaerkeUnits(pair.getRight());
        });

        arzneimittelCluster.setWirkstoff(wirkstoffCluster);
        return arzneimittelCluster;
    }

    private static Optional<Pair<Double, String>> parseStaerke(String staerke) {
        if (staerke.isBlank())
            return Optional.empty();
        String[] staerkeParts = staerke.split(" ");
        if (staerkeParts.length == 2) {
            String einheit = staerkeParts[1];
            String clearString = staerkeParts[0].replaceAll("[^\\d.,]", "");
            clearString = clearString.replace(",", ".");
            log.debug("Parsing magnitude {} and unit {}", clearString, einheit);
            if(clearString.isBlank())
                return Optional.empty();
            double staerkeValue = Double.parseDouble(clearString.replace(",", "."));
            return Optional.of(Pair.of(staerkeValue, einheit));
        }
        return Optional.empty();
    }


    private String checkForm(String form) {
        if (formAliases.containsKey(form)) {
            String oldForm = form;
            form = formAliases.get(form);
            log.info("[FORM]: Changed {} to {} via alias ", oldForm, form);
            validationLogger.info("[FORM]: Changed {} to {} via alias ", oldForm, form);
        } else {
            LDResult result = findClosestWord(form, formDict);
            form = result.closestWord();
            log.info("[FORM]: Found {} for {}", form, result.targetWord());
            validationLogger.info("[FORM]: Found {} for {}", form, result.targetWord());
        }
        return form;
    }

    private String checkEinheit(String einheit) {
        if (einheit.isEmpty()) {
            log.info("[EINHEIT]: Einheit is empty, defaulting to 'E'");
            validationLogger.info("[EINHEIT]: Einheit is empty, defaulting to 'E'");
            return "E";
        }
        LDResult result = findClosestWord(einheit, einheitDict);
        einheit = result.closestWord();
        log.info("[EINHEIT]: Found {} for {}", einheit, result.targetWord());
        validationLogger.info("[EINHEIT]: Found {} for {}", einheit, result.targetWord());
        return einheit;
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
