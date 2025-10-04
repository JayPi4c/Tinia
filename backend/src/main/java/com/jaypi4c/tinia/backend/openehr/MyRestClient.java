package com.jaypi4c.tinia.backend.openehr;

import com.jaypi4c.tinia.backend.autoconfigure.OpenEhrProperties;
import com.nedap.archie.rm.RMObject;
import com.nedap.archie.rm.support.identification.ObjectVersionId;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.ehrbase.openehr.sdk.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.openehr.sdk.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of the DefaultRestClient which adds authentication to the requests.
 * <br>
 * It overwrites rest methods and adds the authentication header.
 */
@Component
public class MyRestClient extends DefaultRestClient {

    private final OpenEhrProperties openEhrProperties;

    public MyRestClient(OpenEhrClientConfig config, TemplateProvider provider, OpenEhrProperties openEhrProperties) {
        super(config, provider);
        this.openEhrProperties = openEhrProperties;
    }

    @Override
    protected ObjectVersionId httpPost(URI uri, RMObject body) {
        Map<String, String> headers = appendAuthHeaders(null);
        return httpPost(uri, body, headers);
    }

    @Override
    protected HttpResponse internalPost(URI uri, Map<String, String> headers, String bodyString, ContentType contentType, String accept) {
        headers = appendAuthHeaders(headers);
        return super.internalPost(uri, headers, bodyString, contentType, accept);
    }

    @Override
    protected HttpResponse internalGet(URI uri, Map<String, String> headers, String accept) {
        headers = appendAuthHeaders(headers);
        return super.internalGet(uri, headers, accept);
    }

    private Map<String, String> appendAuthHeaders(Map<String, String> headers) {
        String auth = openEhrProperties.getUsername() + ":" + openEhrProperties.getPassword();
        String encodedAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        if (headers == null)
            headers = new HashMap<>();
        headers.put("Authorization", encodedAuth);
        return headers;
    }

}
