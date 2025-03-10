package com.jaypi4c.tinia.core;

import com.jaypi4c.tinia.core.openehr.OpenEhrManager;
import com.jaypi4c.tinia.core.openehr.entities.nephromedikationcomposition.NephroMedikationComposition;
import com.jaypi4c.tinia.core.recognition.CellReader;
import com.jaypi4c.tinia.core.recognition.TableExtractor;
import com.jaypi4c.tinia.core.utils.DebugDrawer;
import com.jaypi4c.tinia.core.utils.OutputSaver;
import com.jaypi4c.tinia.core.validation.IActiveIngredientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.ehrbase.openehr.sdk.generator.commons.interfaces.CompositionEntity;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Pipeline {

    private final OpenEhrManager openEhrManager;
    private final TableExtractor tableExtractor;
    private final CellReader cellReader;
    private final DebugDrawer debugDrawer;
    private final OutputSaver outputSaver;
    private final IActiveIngredientValidator validator;

    private Optional<PDDocument> loadDocument(InputStream inputStream) {
        try {
            return Optional.of(PDDocument.load(inputStream));
        } catch (IOException e) {
            log.error("Failed to load document", e);
            return Optional.empty();
        }
    }

    /**
     * <a href="https://www.geeksforgeeks.org/print-2-d-array-matrix-java/">source</a>
     *
     * @param mat 2D String array
     */
    public void print2D(String[][] mat) {
        // Loop through all rows
        for (String[] strings : mat) System.out.println(Arrays.toString(strings));
    }

    public List<CompositionEntity> process(InputStream inputStream, String name) {
        // openEhrManager.checkForTemplate();
        debugDrawer.setCurrentFilename(name);


        List<CompositionEntity> compositionList = new ArrayList<>();

        Optional<PDDocument> documentOpt = loadDocument(inputStream);
        if (documentOpt.isEmpty()) {
            log.error("Failed to load document");
            return Collections.emptyList();
        }
        try (PDDocument document = documentOpt.get()) {
            tableExtractor.setDocument(document);
            cellReader.setDocument(document);
            // openEhrManager.updateIDs();
            int numberOfPages = document.getNumberOfPages();
            for (int page = 0; page < numberOfPages; page++) {
                debugDrawer.setCurrentPage(page);

                tableExtractor.processPage(page);

                if (tableExtractor.wasSuccessful()) {
                    Rectangle2D[][] table = tableExtractor.getTable();
                    String date = tableExtractor.getDate();


                    CellReader.ReadingResult result = cellReader.processPage(page, date, table);
                    if (result.hasTable()) {
                        print2D(result.table());
                        outputSaver.saveTable(name, result.table(), page, date);
                        NephroMedikationComposition composition = openEhrManager.createComposition(result);
                        compositionList.add(composition);
                        //  boolean success = openEhrManager.sendNephroMedikationData(composition);
                        //if (success)
                        //     log.info("Successfully sent data to EHRBase");
                        // else
                        //     log.error("Failed to send data to EHRBase");
                    }
                } else {
                    log.info("No medication table found");
                }
                log.info("Finished page {}, {}%", page + 1, (page + 1) * 100 / numberOfPages);
            }
        } catch (IOException e) {
            log.error("Error while reading pdf", e);
        }
        validator.finish();
        outputSaver.save("output");
        return compositionList;

    }
}
