package com.jaypi4c.openehr.compositions;

import com.jaypi4c.openehr.compositions.nephromedikationcomposition.NephroMedikationComposition;
import com.jaypi4c.openehr.compositions.nephromedikationcomposition.NephroMedikationTemplateProvider;
import com.jaypi4c.openehr.compositions.nephromedikationcomposition.definition.*;
import com.nedap.archie.rm.RMObject;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartySelf;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.ehrbase.client.flattener.Unflattener;
import org.ehrbase.client.openehrclient.CompositionEndpoint;
import org.ehrbase.client.openehrclient.EhrEndpoint;
import org.ehrbase.client.openehrclient.OpenEhrClient;
import org.ehrbase.serialisation.jsonencoding.CanonicalJson;
import org.ehrbase.webtemplate.templateprovider.TemplateProvider;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Factory class which will later be used to create compositions and send them to an EhrBase instance.
 * <br>
 * For now, it is just used for testing purposes.
 * <br>
 * @see <a href="https://ckm.highmed.org/ckm/templates/1246.169.1019">Nephro_Medikation in CKM</a> for more information.
 */
public class CompositionFactory {

    public static void main(String[] args) throws URISyntaxException {

        NephroMedikationComposition composition = prepareComposition(new NephroMedikationComposition());

        // setting Fallidentifikation
        FallidentifikationCluster fallidentifikation = new FallidentifikationCluster();
        fallidentifikation.setFallKennungValue("Medikationsplan 1");
        composition.setFallidentifikation(fallidentifikation);


        // jede Zeile in der Tabelle ist eine Verordnung. So kann eine ganze Tabelle in einer composition gespeichert werden.
        List<VerordnungVonArzneimittelInstruction> list = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            VerordnungVonArzneimittelInstruction arzneimittel = prepareInstruction(new VerordnungVonArzneimittelInstruction());

            VerordnungVonArzneimittelVerordnungActivity verordnung = new VerordnungVonArzneimittelVerordnungActivity();
            verordnung.setHandelsnameValue("Handelsname " + i);

            ArzneimittelCluster arzneimittelCluster = new ArzneimittelCluster();
            ArzneimittelDarreichungsformElement arzneimittelDarreichungsformElement = new ArzneimittelDarreichungsformElement();
            arzneimittelDarreichungsformElement.setValue("Tabletten");
            arzneimittelCluster.setDarreichungsform(List.of(arzneimittelDarreichungsformElement));
            verordnung.setArzneimittel(arzneimittelCluster);


            arzneimittel.setVerordnung(List.of(verordnung));
            list.add(arzneimittel);
        }

        composition.setVerordnungVonArzneimittel(list);


        TemplateProvider provider = new NephroMedikationTemplateProvider();

        Unflattener unflat = new Unflattener(provider);
        RMObject rmObject = unflat.unflatten(composition);

        CanonicalJson json = new CanonicalJson();
        System.out.println(json.marshal(rmObject));

        // see https://ehrbase.readthedocs.io/en/latest/02_getting_started/04_create_ehr/index.html#client-library
        OpenEhrClient openEhrClient = DefaultRestClientHelper.setupRestClient();
        EhrEndpoint ehrEndpoint = openEhrClient.ehrEndpoint();
        UUID ehr = ehrEndpoint.createEhr();

        CompositionEndpoint compositionEndpoint = openEhrClient.compositionEndpoint(ehr);
        compositionEndpoint.mergeCompositionEntity(composition);
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
