package com.jaypi4c.tinia.extractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ExtractorApp {

    static void main(String[] args) {
        SpringApplication.run(ExtractorApp.class, args);
    }

}