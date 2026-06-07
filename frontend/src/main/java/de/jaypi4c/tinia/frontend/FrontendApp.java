package de.jaypi4c.tinia.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FrontendApp {

    static void main(String[] args) {
        SpringApplication.run(FrontendApp.class, args);
    }

}
