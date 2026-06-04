package de.jaypi4c.tinia.extractor.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinia.extractor.debug")
public class DebugProperties {

    private boolean drawImages = false;
    private String folder = "./io/debug/";

}
