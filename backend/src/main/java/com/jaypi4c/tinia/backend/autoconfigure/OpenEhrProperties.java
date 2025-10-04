package com.jaypi4c.tinia.backend.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinia.openehr")
public class OpenEhrProperties {
    
    private String username;
    private String password;
    private String url;
}
