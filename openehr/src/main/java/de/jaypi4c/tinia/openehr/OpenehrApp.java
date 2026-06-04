package de.jaypi4c.tinia.openehr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OpenehrApp {
    static void main(String[] args) {
        SpringApplication.run(OpenehrApp.class, args);
    }

}
