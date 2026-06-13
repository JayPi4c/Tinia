package de.jaypi4c.tinia.detector.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinia.detector.debug")
public class DebugProperties {

    private boolean drawImages = false;
    private String folder = "./io/debug/";

}
