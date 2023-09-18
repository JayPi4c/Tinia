package com.jaypi4c.openehr;

import com.jaypi4c.openehr.compositions.nephromedikationcomposition.NephroMedikationTemplateProvider;
import org.ehrbase.client.openehrclient.OpenEhrClientConfig;
import org.ehrbase.client.openehrclient.defaultrestclient.DefaultRestClient;
import org.ehrbase.webtemplate.templateprovider.TemplateProvider;

import java.net.URI;
import java.net.URISyntaxException;

public class DefaultRestClientHelper {

    private static final String OPEN_EHR_URL = "http://localhost:8080/ehrbase/";

    /**
     * Creates a new {@link DefaultRestClient} with the {@link NephroMedikationTemplateProvider}
     * <br>
     * It expects an openEHR server running on {@link #OPEN_EHR_URL}
     *
     * @return a new {@link DefaultRestClient}
     * @throws URISyntaxException if the {@link #OPEN_EHR_URL} is not a valid URI
     */
    public static DefaultRestClient setupRestClient() throws URISyntaxException {
        TemplateProvider provider = new NephroMedikationTemplateProvider();
        return new MyRestClient(new OpenEhrClientConfig(new URI(OPEN_EHR_URL)), provider);
    }


}
