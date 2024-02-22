package com.jaypi4c.tinia.pipeline;

import com.jaypi4c.tinia.pipeline.openehr.OpenEhrManager;
import com.jaypi4c.tinia.pipeline.recognition.CellReader;
import com.jaypi4c.tinia.pipeline.recognition.TableExtractor;
import com.jaypi4c.tinia.pipeline.utils.DebugDrawer;
import com.jaypi4c.tinia.pipeline.utils.OutputSaver;
import com.jaypi4c.tinia.pipeline.validation.IActiveIngredientValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

    private static File[] getFiles(String path) {
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

    private static int getNumberOfPages(File in) {
        try (PDDocument document = PDDocument.load(in)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            log.error("Failed to get number of pages", e);
            return -1;
        }
    }

    /**
     * <a href="https://www.geeksforgeeks.org/print-2-d-array-matrix-java/">source</a>
     *
     * @param mat 2D String array
     */
    public static void print2D(String[][] mat) {
        // Loop through all rows
        for (String[] strings : mat) System.out.println(Arrays.toString(strings));
    }

    public String process(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            int pages = document.getNumberOfPages();
            StringBuilder text = new StringBuilder();
            for (int page = 0; page < pages; page++) {
                stripper.setStartPage(page + 1);
                stripper.setEndPage(page + 1);
                text.append(stripper.getText(document));
                if (page < pages - 1) text.append("\n");
            }
            return text.toString();
        } catch (IOException e) {
            return "Reading PDF failed";
        }
    }

    public void start() {

        openEhrManager.checkForTemplate();

        File[] files = getFiles(ioFolder);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            log.info("Starting with file {}", file.getName());

            debugDrawer.setCurrentFilename(file.getName());
            tableExtractor.setCurrentFile(file);
            cellReader.setPdfFile(file);
            openEhrManager.updateIDs();
            int numberOfPages = getNumberOfPages(file);
            for (int page = 0; page < numberOfPages; page++) {
                debugDrawer.setCurrentPage(page);

                tableExtractor.processPage(page);

                if (tableExtractor.wasSuccessful()) {
                    Rectangle2D[][] table = tableExtractor.getTable();
                    String date = tableExtractor.getDate();


                    CellReader.ReadingResult result = cellReader.processPage(page, date, table);
                    if (result.hasTable()) {
                        print2D(result.table());
                        outputSaver.saveTable(file.getName(), result.table(), page, date);
                        boolean success = openEhrManager.sendNephroMedikationData(result);
                        if (success)
                            log.info("Successfully sent data to EHRBase");
                        else
                            log.error("Failed to send data to EHRBase");
                    }
                } else {
                    log.info("No medication table found");
                }
                log.info("Finished page {}, {}%", page + 1, (page + 1) * 100 / numberOfPages);
            }

            cellReader.finish();
            tableExtractor.finish();
            log.info("Finished file {}, {}%", file.getName(), (i + 1) * 100 / files.length);
        }
        validator.finish();
        outputSaver.save("output");
    }
}
