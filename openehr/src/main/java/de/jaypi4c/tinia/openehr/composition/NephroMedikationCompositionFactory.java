package de.jaypi4c.tinia.openehr.composition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import de.jaypi4c.tinia.openehr.entities.nephromedikationcomposition.NephroMedikationComposition;
import de.jaypi4c.tinia.openehr.entities.nephromedikationcomposition.definition.FallidentifikationCluster;
import de.jaypi4c.tinia.openehr.entities.nephromedikationcomposition.definition.VerordnungVonArzneimittelInstruction;
import de.jaypi4c.tinia.openehr.util.CombinedDosisschemaParser;
import de.jaypi4c.tinia.openehr.util.StandardDosisschemaFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequiredArgsConstructor
public class NephroMedikationCompositionFactory implements CompositionFactory<NephroMedikationComposition> {

    private final CombinedDosisschemaParser combinedDosisschemaParser;
    private final StandardDosisschemaFactory standardDosisschemaParser;

/*
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
    }*/

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

    @Override
    public String getTemplateId() {
        return "Nephro_Medikation";
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
