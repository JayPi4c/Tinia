package de.jaypi4c.tinia.frontend.service.impl;

import com.nedap.archie.rm.RMObject;
import de.jaypi4c.tinia.frontend.dto.EntryDto;
import de.jaypi4c.tinia.frontend.service.OpenEhrService;
import lombok.RequiredArgsConstructor;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClient;
import org.ehrbase.openehr.sdk.generator.commons.aql.query.Query;
import org.ehrbase.openehr.sdk.generator.commons.aql.record.Record2;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.ehrbase.openehr.sdk.response.dto.TemplatesResponseData;
import org.ehrbase.openehr.sdk.response.dto.ehrscape.TemplateMetaDataDto;
import org.ehrbase.openehr.sdk.serialisation.RMDataFormat;
import org.ehrbase.openehr.sdk.serialisation.dto.GeneratedDtoToRmConverter;
import org.ehrbase.openehr.sdk.serialisation.walker.defaultvalues.DefaultValues;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenEhrServiceImpl implements OpenEhrService {

    private final OpenEhrClient ehrClient;
    private final TemplateProvider templateProvider;

    private String convertToJson(CompositionEntity composition) {
        RMObject rmObject = new GeneratedDtoToRmConverter(templateProvider, _ -> new DefaultValues())
                .toRMObject(composition);
        return RMDataFormat.canonicalJSON().marshal(rmObject);
    }

    @Override
    public List<EntryDto> fetchAll() {
        Query<Record2<String, String>> query = Query.buildNativeQuery("SELECT " +
                "e/ehr_id/value, " +
                "c/uid/value " +
                "FROM " +
                "EHR e " +
                "CONTAINS " +
                "COMPOSITION c", String.class, String.class);
        List<Record2<String, String>> result = ehrClient.aqlEndpoint().execute(query);
        return result.stream()
                .map(r -> new EntryDto(
                        r.value1(),
                        r.value2()))
                .toList();
    }

    @Override
    public List<String> fetchAllTemplates() {
        TemplatesResponseData response = ehrClient.templateEndpoint().findAllTemplates();
        return response.get().stream().map(TemplateMetaDataDto::getTemplateId).toList();
    }
}
