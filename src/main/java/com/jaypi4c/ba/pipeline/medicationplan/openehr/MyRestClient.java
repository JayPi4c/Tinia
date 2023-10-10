package com.jaypi4c.ba.pipeline.medicationplan.openehr;

import com.nedap.archie.rm.RMObject;
import com.nedap.archie.rm.support.identification.ObjectVersionId;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.openehr.sdk.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

/**
 * Custom implementation of the DefaultRestClient which adds authentication to the requests.
 * <br>
 * It just overrides the {@link #httpPost(URI, RMObject)} method and adds the authentication header.
 */
@Component
public class MyRestClient extends DefaultRestClient {

    @Value("${openehr.username}")
    private String username;
    @Value("${openehr.password}")
    private String password;

    @Autowired
    public MyRestClient(OpenEhrClientConfig config, TemplateProvider provider) {
        super(config, provider);
    }

    @Override
    protected ObjectVersionId httpPost(URI uri, RMObject body) {
        String auth = username + ":" + password;
        String encodedAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        Map<String, String> headers = Map.of("Authorization", encodedAuth);
        return httpPost(uri, body, headers);
    }

}
