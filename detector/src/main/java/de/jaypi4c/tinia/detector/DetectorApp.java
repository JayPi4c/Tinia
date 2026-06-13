package de.jaypi4c.tinia.detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DetectorApp {

    static void main(String[] args) {
        SpringApplication.run(DetectorApp.class, args);
    }

}
