package de.jaypi4c.tinia.openehr.config;

import de.jaypi4c.tinia.openehr.autoconfigure.OpenEhrProperties;
import lombok.RequiredArgsConstructor;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;


@Configuration
@RequiredArgsConstructor
public class RestConfig {
    private final OpenEhrProperties openEhrProperties;

    @Bean
    public OpenEhrClientConfig getOpenEhrClientConfig() {
        try {
            return new OpenEhrClientConfig(new URI(openEhrProperties.getUrl()));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Bean
    public OpenEhrClient getDefaultRestClient(OpenEhrClientConfig config, TemplateProvider templateProvider) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(openEhrProperties.getUsername(), openEhrProperties.getPassword())
        );
        HttpClient hc = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        return new DefaultRestClient(config, templateProvider, hc);
    }

}
