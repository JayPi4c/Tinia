package com.jaypi4c.tinia.pipeline;

import com.jaypi4c.tinia.pipeline.openehr.OpenEhrManager;
import com.jaypi4c.tinia.pipeline.openehr.compositions.nephromedikationcomposition.NephroMedikationComposition;
import com.jaypi4c.tinia.pipeline.recognition.CellReader;
import com.jaypi4c.tinia.pipeline.recognition.TableExtractor;
import com.jaypi4c.tinia.pipeline.utils.DebugDrawer;
import com.jaypi4c.tinia.pipeline.utils.OutputSaver;
import com.jaypi4c.tinia.pipeline.validation.IActiveIngredientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
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
    @Value("${io.dataFolder}")
    private String ioFolder;

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

    private File[] getFiles(String path) {
        File folder = new File(path);
        File[] entries = folder.listFiles();
        if (entries == null) {
            log.error("Could not find folder {}", path);
            System.exit(-1);
        }
        return Arrays.stream(entries)
                .filter(file -> file.getName().endsWith(".pdf"))
                .toArray(File[]::new);
    }

    public List<String> process(InputStream inputStream, String name) {
        List<String> compositionList = new ArrayList<>();

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
                        compositionList.add(openEhrManager.convertToJson(composition));
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
        return compositionList;

    }

    public void start() {

        openEhrManager.checkForTemplate();

        File[] files = getFiles(ioFolder);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            log.info("Starting with file {}", file.getName());

            debugDrawer.setCurrentFilename(file.getName());

            // create InputStream for file
            try (InputStream inputStream = new FileInputStream(file)) {
                process(inputStream, file.getName());
            } catch (IOException e) {
                log.error("Error while reading file", e);
            }
            log.info("Finished file {}, {}%", file.getName(), (i + 1) * 100 / files.length);
        }
        validator.finish();
        outputSaver.save("output");
    }
}
