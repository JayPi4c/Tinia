package com.jaypi4c.ba.pipeline.medicationplan.config;

import com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions.nephromedikationcomposition.NephroMedikationTemplateProvider;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RestConfig {

    @Value("${openehr.url}")
    private String OPEN_EHR_URL;

    @Bean
    public TemplateProvider getTemplateProvider() {
        return new NephroMedikationTemplateProvider();
    }

    @Bean
    public OpenEhrClientConfig getOpenEhrClientConfig() {
        try {
            return new OpenEhrClientConfig(new URI(OPEN_EHR_URL));
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
