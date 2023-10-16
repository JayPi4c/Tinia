package com.jaypi4c.ba.pipeline.medicationplan.validation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Uses the website gelbe-liste.de to validate active ingredients.
 */
@Slf4j
@Component
@Getter
public class GelbeListeActiveIngredientValidator implements IActiveIngredientValidator {

    private int validationCount = 0;
    private int validationFailureCount = 0;

    @Override
    public boolean validate(String activeIngredient) {
        if (activeIngredient == null || activeIngredient.isEmpty()) {
            log.warn("Active ingredient is null or empty");
            return false;
        }
        return request(activeIngredient);
    }

    @Override
    public void finish() {
        log.info("Validation count: {}", validationCount);
        log.info("Validation failure count: {}", validationFailureCount);
        log.info("Validation success rate: {}", (double) (validationCount - validationFailureCount) / validationCount);
    }

    private boolean request(String searchString) {
        final String[] parts = searchString.split(" ");

        final String query = String.join("%20", parts);

        // dailymed for english active ingredients
        final String url = "https://www.gelbe-liste.de/suche?term=" + query;
        try {
            validationCount++;
            Document doc = Jsoup.connect(url).get();
            return checkBody(doc, parts);
        } catch (IOException ex) {
            validationFailureCount++;
            log.error("Failed to request data. Probably a connection error.");
        }
        return false;
    }

    private boolean checkBody(Document doc, String[] queryParts) {
        if (doc == null)
            return false;
        try {
            Element elt = doc.getElementById("products");
            if (elt == null)
                return false; // failed to find a product list in document
            Elements list = elt.getElementsByClass("m-0"); // all products have class m-0
            List<String> products = list.stream().map(Element::text).toList();
            for (String product : products) {
                if (containsAll(product, queryParts))
                    return true;
            }
        } catch (Exception e) {
            log.error("Error while parsing xml");
        }

        return false;
    }

    private boolean containsAll(String product, String[] queryParts) {
        product = product.toLowerCase();
        for (String part : queryParts) {
            if (!product.contains(part.toLowerCase()))
                return false;
        }
        return true;
    }

}
