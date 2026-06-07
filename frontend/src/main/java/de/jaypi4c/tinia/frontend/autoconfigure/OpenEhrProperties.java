package de.jaypi4c.tinia.frontend.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinia.frontend.openehr")
public class OpenEhrProperties {

    private String username;
    private String password;
    private String url;

}
