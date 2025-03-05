package com.jaypi4c.tinia.core.config;

import com.jaypi4c.tinia.core.openehr.bmp.NephroMedikationTemplateProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClient;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.openehr.sdk.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RestConfig {

    @Value("${openehr.url:localhost}")
    private String OPEN_EHR_URL;
    @Value("${openehr.username}")
    private String username;
    @Value("${openehr.password}")
    private String password;

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

    @Bean
    public OpenEhrClient getDefaultRestClient(OpenEhrClientConfig config, TemplateProvider templateProvider) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password)
        );
        HttpClient hc = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        return new DefaultRestClient(config, templateProvider, hc);
    }

}
