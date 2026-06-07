package de.jaypi4c.tinia.openehr.service.impl;

import com.nedap.archie.rm.datavalues.DvText;
import com.nedap.archie.rm.ehr.EhrStatus;
import com.nedap.archie.rm.generic.PartySelf;
import com.nedap.archie.rm.support.identification.GenericId;
import com.nedap.archie.rm.support.identification.PartyRef;
import de.jaypi4c.tinia.openehr.composition.CompositionFactory;
import de.jaypi4c.tinia.openehr.service.NetworkService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.client.openehrclient.CompositionEndpoint;
import org.ehrbase.openehr.sdk.client.openehrclient.EhrEndpoint;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClient;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.ehrbase.openehr.sdk.util.exception.WrongStatusCodeException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkServiceImpl implements NetworkService {

    private final OpenEhrClient ehrClient;
    private final CompositionFactory<?> compositionFactory;
    private EhrEndpoint ehrEndpoint;

    @PostConstruct
    private void init() {
        ehrEndpoint = ehrClient.ehrEndpoint();
        ehrClient.templateEndpoint().ensureExistence(compositionFactory.getTemplateId());
    }

    @Override
    public CompositionEntity sendData(CompositionEntity composition) {
        // TODO: implement logic to only create one EHR per patient
        UUID applicationUserID = UUID.randomUUID(); // TODO: change to jobID
        UUID ehrID = ehrEndpoint.createEhr(createEhrStatus(applicationUserID));
        try {
            CompositionEndpoint compositionEndpoint = ehrClient.compositionEndpoint(ehrID);
            return compositionEndpoint.mergeCompositionEntity(composition);
        } catch (WrongStatusCodeException wsce) {
            log.error("Error while sending composition to EHRBase", wsce);
            return null;
        }
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
