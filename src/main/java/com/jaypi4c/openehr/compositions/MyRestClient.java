package com.jaypi4c.openehr.compositions;

import com.nedap.archie.rm.RMObject;
import org.ehrbase.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.client.openehrclient.VersionUid;
import org.ehrbase.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.webtemplate.templateprovider.TemplateProvider;

import java.net.URI;
import java.util.Map;

/**
 * Custom implementation of the DefaultRestClient which adds authentication to the requests.
 * <br>
 * It just overrides the {@link #httpPost(URI, RMObject)} method and adds the authentication header.
 */
public class MyRestClient extends DefaultRestClient {

    private String username, password;

    public MyRestClient(OpenEhrClientConfig config, TemplateProvider provider) {
        super(config, provider);
        username = "ehrbase-user";
        password = "SuperSecretPassword";
    }

    @Override
    protected VersionUid httpPost(URI uri, RMObject body) {
        String auth = username + ":" + password;
        String encodedAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        Map<String, String> headers = Map.of("Authorization", encodedAuth);
        return httpPost(uri, body, headers);
    }

}
