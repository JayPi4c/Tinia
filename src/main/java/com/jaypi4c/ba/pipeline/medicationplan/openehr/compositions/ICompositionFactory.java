package com.jaypi4c.ba.pipeline.medicationplan.openehr.compositions;

public interface ICompositionFactory<T> {

    /**
     * Creates a composition with the given parameters.
     *
     * @param args 2D String array, the first row is the header of the data table
     * @param date the date when the medication plan was printed
     * @return Composition with the given parameters
     */
    T createComposition(String[][] args, String date);
}
