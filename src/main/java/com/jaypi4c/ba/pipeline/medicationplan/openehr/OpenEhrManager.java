package com.jaypi4c.ba.pipeline.medicationplan.openehr;


import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.NephroMedikationComposition;
import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.NephroMedikationCompositionFactory;
import com.nedap.archie.rm.datavalues.DvText;
import com.nedap.archie.rm.ehr.EhrStatus;
import com.nedap.archie.rm.generic.PartySelf;
import com.nedap.archie.rm.support.identification.GenericId;
import com.nedap.archie.rm.support.identification.PartyRef;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.client.openehrclient.CompositionEndpoint;
import org.ehrbase.client.openehrclient.EhrEndpoint;
import org.ehrbase.client.openehrclient.OpenEhrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class OpenEhrManager {

    // see https://ehrbase.readthedocs.io/en/latest/02_getting_started/04_create_ehr/index.html#client-library
    private final OpenEhrClient openEhrClient;
    private final EhrEndpoint ehrEndpoint;
    private final NephroMedikationCompositionFactory nephroMedikationCompositionFactory;

    @Value("${openehr.username}")
    private String username;
    @Value("${openehr.password}")
    private String password;

    @Value("${openehr.url}")
    private String OPENEHR_URL;

    /**
     * the ID for the Arztbrief on application level
     */
    private UUID applicationUserID;
    /**
     * The ID for the EHR returned from EHRBase
     */
    private UUID ehrID;


    @Autowired
    public OpenEhrManager(OpenEhrClient openEhrClient, NephroMedikationCompositionFactory factory) {
        this.openEhrClient = openEhrClient;
        this.ehrEndpoint = openEhrClient.ehrEndpoint();

        nephroMedikationCompositionFactory = factory;
    }

    public void checkForTemplate(final String TEMPLATE_NAME) {
        URI uri = URI.create(OPENEHR_URL + "rest/openehr/v1/definition/template/adl1.4/" + TEMPLATE_NAME);
        HttpClient client = HttpClient.newHttpClient();

        String auth = username + ":" + password;
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("Authorization", encodedAuth)
                .header("accept", "application/xml")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.info("Template does not exist, creating it");
                uri = URI.create(OPENEHR_URL + "rest/openehr/v1/definition/template/adl1.4");
                client = HttpClient.newHttpClient();

                request = HttpRequest
                        .newBuilder()
                        .uri(uri)
                        .header("Authorization", encodedAuth)
                        .header("accept", "application/xml")
                        .header("Content-Type", "application/xml")
                        .POST(HttpRequest.BodyPublishers.ofString(loadTemplate("/templates/" + TEMPLATE_NAME + ".opt")))
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 204) {
                    log.info("Template created");
                } else {
                    log.error("Failed to upload new template.");
                }
            } else {
                log.info("Template exists");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error while checking for template", e);
        }
    }

    public static String loadTemplate(String path) {
        StringBuilder template = new StringBuilder();

        try (InputStream is = OpenEhrManager.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                template.append(line);
            }
        } catch (IOException e) {
            log.error("Failed to load template", e);
        }
        return template.toString();
    }

    public void updateIDs() {
        applicationUserID = UUID.randomUUID();
        ehrID = ehrEndpoint.createEhr(createEhrStatus(applicationUserID));
    }

    public boolean sendNephroMedikationData(String[][] medicationMatrix) {
        if (applicationUserID == null || ehrID == null) {
            updateIDs();
        }
        NephroMedikationComposition composition = nephroMedikationCompositionFactory.createComposition(medicationMatrix);

        CompositionEndpoint compositionEndpoint = openEhrClient.compositionEndpoint(ehrID);
        compositionEndpoint.mergeCompositionEntity(composition);
        return true;
    }

    private EhrStatus createEhrStatus(UUID applicationUserID) {
        EhrStatus status = new EhrStatus();
        status.setArchetypeNodeId("openEHR-EHR-ITEM_TREE.generic.v1");

        DvText name = new DvText();
        name.setValue("any EHR STATUS");
        status.setName(name);

        PartySelf subject = new PartySelf();
        PartyRef externalRef = new PartyRef();
        GenericId id = new GenericId();
        id.setValue(applicationUserID.toString());
        id.setScheme("id_scheme");
        externalRef.setId(id);
        externalRef.setNamespace("BA");
        externalRef.setType("PERSON");
        subject.setExternalRef(externalRef);
        status.setSubject(subject);

        status.setModifiable(true);
        status.setQueryable(true);

        return status;
    }


}
