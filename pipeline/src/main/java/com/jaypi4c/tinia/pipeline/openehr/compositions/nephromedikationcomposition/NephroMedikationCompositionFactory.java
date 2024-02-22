package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition;

import com.google.gson.Gson;
import com.jaypi4c.tinia.pipeline.openehr.compositions.ICompositionFactory;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.FallidentifikationCluster;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.definition.VerordnungVonArzneimittelInstruction;
import com.jaypi4c.tinia.pipeline.utils.WordUtils;
import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.archetyped.FeederAuditDetails;
import com.nedap.archie.rm.datavalues.encapsulated.DvEncapsulated;
import com.nedap.archie.rm.datavalues.encapsulated.DvParsable;
import com.nedap.archie.rm.generic.PartyIdentified;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private final CombinedDosisschemaParser combinedDosisschemaParser;
    private final StandardDosisschemaFactory standardDosisschemaParser;

    @Autowired
    public NephroMedikationCompositionFactory(CombinedDosisschemaParser cdp, StandardDosisschemaFactory sdp) {
        this.combinedDosisschemaParser = cdp;
        this.standardDosisschemaParser = sdp;
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
            VerordnungVonArzneimittelInstruction arzneimittel = switch (row.length) {
                case 8 -> combinedDosisschemaParser.parse(date, row);
                case 11 -> standardDosisschemaParser.parse(date, row);
                default -> standardDosisschemaParser.parse(date, row);
            };
            list.add(arzneimittel);
        }

        composition.setVerordnungVonArzneimittel(list);

        return composition;
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
