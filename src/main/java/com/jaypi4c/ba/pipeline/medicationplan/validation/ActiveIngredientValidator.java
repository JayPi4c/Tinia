package com.jaypi4c.ba.pipeline.medicationplan.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ActiveIngredientValidator {
    private final RestTemplate restTemplate;

    public ActiveIngredientValidator(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public boolean validate(String activeIngredient) {
        if (activeIngredient == null || activeIngredient.isEmpty()) {
            log.warn("Active ingredient is null or empty");
            return false;
        }
        request(activeIngredient);
        return true;
    }

    private void request(String searchString) {
        // dailymed for english active ingredients
        final HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        final String url = "https://www.gelbe-liste.de/suche?term=" + searchString.replaceAll(" ", "%20");

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        final String body = response.getBody();
        if (body.contains(searchString)) {
            log.info("Found {} on gelbe-liste.de", searchString);
        } else {
            log.error("Did not find {} on gelbe-liste.de", searchString);
            log.debug("Response body: {}", body);
        }
    }

}
