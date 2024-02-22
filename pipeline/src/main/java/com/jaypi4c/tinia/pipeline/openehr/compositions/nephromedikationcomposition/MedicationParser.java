package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition;

import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.ArzneimittelCluster;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.ArzneimittelDarreichungsformElement;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.VerordnungVonArzneimittelInstruction;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.WirkstoffCluster;
import com.jaypi4c.tinia.pipeline.utils.WordUtils;
import com.jaypi4c.tinia.pipeline.validation.IActiveIngredientValidator;
import com.jaypi4c.tinia.pipeline.validation.helpers.DarreichungsformHelper;
import com.jaypi4c.tinia.pipeline.validation.helpers.EinheitenHelper;
import com.nedap.archie.rm.generic.PartySelf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jaypi4c.tinia.pipeline.utils.WordUtils.findClosestWord;

@Slf4j
abstract class MedicationParser {

    static final Logger validationLogger = LoggerFactory.getLogger("VALIDATION-LOGGER");
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    final IActiveIngredientValidator validator;
    private final String[] formDict;
    private final String[] einheitDict;
    private final Map<String, String> formAliases;
    @Value("${validation.active}")
    boolean validationActive;

    MedicationParser(IActiveIngredientValidator validator, DarreichungsformHelper darreichungsformHelper, EinheitenHelper einheitenHelper) {
        this.validator = validator;
        formDict = darreichungsformHelper.getDictionary(); //loadDictionary("/dictionaries/DarreichungsformAllowlist.txt");
        formAliases = darreichungsformHelper.getAliases();//loadAliases("/aliases/alias.json");
        einheitDict = einheitenHelper.getDictionary();
    }


    abstract VerordnungVonArzneimittelInstruction parse(String date, String[] row);

    void validateWirkstoff(String wirkstoff) {
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
    }

    String checkForm(String form) {
        if (formAliases.containsKey(form)) {
            String oldForm = form;
            form = formAliases.get(form);
            log.info("[FORM]: Changed {} to {} via alias ", oldForm, form);
            validationLogger.info("[FORM]: Changed {} to {} via alias ", oldForm, form);
        } else {
            WordUtils.LDResult result = findClosestWord(form, formDict);
            form = result.closestWord();
            log.info("[FORM]: Found {} for {}", form, result.targetWord());
            validationLogger.info("[FORM]: Found {} for {}", form, result.targetWord());
        }
        return form;
    }

    String checkEinheit(String einheit) {
        if (einheit.isEmpty()) {
            log.info("[EINHEIT]: Einheit is empty, defaulting to 'E'");
            validationLogger.info("[EINHEIT]: Einheit is empty, defaulting to 'E'");
            return "E";
        }
        WordUtils.LDResult result = findClosestWord(einheit, einheitDict);
        einheit = result.closestWord();
        log.info("[EINHEIT]: Found {} for {}", einheit, result.targetWord());
        validationLogger.info("[EINHEIT]: Found {} for {}", einheit, result.targetWord());
        return einheit;
    }

    VerordnungVonArzneimittelInstruction prepareInstruction(VerordnungVonArzneimittelInstruction
                                                                    instruction) {
        instruction.setLanguage(Language.DE);
        instruction.setNarrativeValue("Lorem ispum");
        instruction.setSubject(new PartySelf());
        return instruction;
    }

    ArzneimittelCluster getArzneimittelCluster(String form, String wirkstoff, String staerke) {
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

    Optional<Pair<Double, String>> parseStaerke(String staerke) {
        if (staerke.isBlank())
            return Optional.empty();
        String[] staerkeParts = staerke.split(" ");
        if (staerkeParts.length == 2) {
            String einheit = staerkeParts[1];
            String clearString = staerkeParts[0].replaceAll("[^\\d.,]", "");
            clearString = clearString.replace(",", ".");
            log.debug("Parsing magnitude {} and unit {}", clearString, einheit);
            if (clearString.isBlank())
                return Optional.empty();
            double staerkeValue = Double.parseDouble(clearString.replace(",", "."));
            return Optional.of(Pair.of(staerkeValue, einheit));
        }
        return Optional.empty();
    }


}
