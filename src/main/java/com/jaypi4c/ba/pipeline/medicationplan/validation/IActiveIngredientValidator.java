package com.jaypi4c.ba.pipeline.medicationplan.validation;

/**
 * Validates active ingredients.
 */
public interface IActiveIngredientValidator {
    /**
     * Validates the given active ingredient.
     *
     * @param activeIngredient the active ingredient to validate
     * @return true if the active ingredient exists and is valid, false otherwise
     */
    boolean validate(String activeIngredient);

    /**
     * This method can be used to obtain final information from the validation process.
     * <p>
     * For example, the number of validation requests and the number of failed requests can be printed.
     */
    void finish();
}
