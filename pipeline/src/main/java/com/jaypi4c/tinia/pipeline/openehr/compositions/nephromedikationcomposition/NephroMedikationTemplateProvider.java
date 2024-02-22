package com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition;

import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.XmlException;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.openehr.schemas.v1.OPERATIONALTEMPLATE;
import org.openehr.schemas.v1.TemplateDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @see <a href="https://www.youtube.com/watch?v=3SykJkbnT34">Generating openEHR Compositions using EHRbase SDK | openEHR Series - Part 4-2</a> YouTube video for more information.
 */
@Slf4j
public class NephroMedikationTemplateProvider implements TemplateProvider {

    @Override
    public Optional<OPERATIONALTEMPLATE> find(String templateId) {
        if (!templateId.equals("Nephro_Medikation"))
            return Optional.empty();
        try (InputStream stream = getClass().getResourceAsStream("/templates/Nephro_Medikation.opt")) {
            TemplateDocument template = TemplateDocument.Factory.parse(stream);
            return Optional.ofNullable(template.getTemplate());
        } catch (XmlException | IOException e) {
            log.error("Error while reading template", e);
            return Optional.empty();
        }
    }

}
