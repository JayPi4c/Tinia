package com.jaypi4c.tinia.pipeline.openehr.compositions;

public interface ICompositionFactory<T> {

    /**
     * Creates a composition with the given parameters.
     *
     * @param args     2D String array, the first row is the header of the data table
     * @param date     the date when the medication plan was printed
     * @param metadata the metadata of the pdf file
     * @return Composition with the given parameters
     */
    T createComposition(String[][] args, String date, String metadata);
}
