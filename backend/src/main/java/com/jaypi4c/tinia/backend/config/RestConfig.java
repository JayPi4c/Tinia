package com.jaypi4c.tinia.backend.config;

import com.jaypi4c.tinia.backend.autoconfigure.OpenEhrProperties;
import com.jaypi4c.tinia.backend.openehr.compositions.nephromedikationcomposition.NephroMedikationTemplateProvider;
import lombok.RequiredArgsConstructor;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
@RequiredArgsConstructor
public class RestConfig {
    private final OpenEhrProperties openEhrProperties;

    @Bean
    public TemplateProvider getTemplateProvider() {
        return new NephroMedikationTemplateProvider();
    }

    @Bean
    public OpenEhrClientConfig getOpenEhrClientConfig() {
        try {
            return new OpenEhrClientConfig(new URI(openEhrProperties.getUrl()));
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
