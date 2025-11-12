package de.jaypi4c.tinia.extractor.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinia.extractor")
public class ExtractorProperties {

    private boolean skipWhenNoHeaderFound = true;

    private Pdf pdf = new Pdf();

    @Data
    public static class Pdf {
        private int dpi = 300;
    }

}
