package com.jaypi4c.ba.pipeline.medicationplan;

import com.jaypi4c.ba.pipeline.medicationplan.openehr.OpenEhrManager;
import com.jaypi4c.ba.pipeline.medicationplan.recognition.CellReader;
import com.jaypi4c.ba.pipeline.medicationplan.recognition.TableExtractor;
import com.jaypi4c.ba.pipeline.medicationplan.utils.DebugDrawer;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class Pipeline {

    @Value("${io.dataFolder}")
    private String ioFolder;

    private final OpenEhrManager openEhrManager;
    private final TableExtractor tableExtractor;
    private final CellReader cellReader;
    private final DebugDrawer debugDrawer;

    @Autowired
    Pipeline(OpenEhrManager openEhrManager, TableExtractor tableExtractor, CellReader cellReader, DebugDrawer debugDrawer) {
        this.openEhrManager = openEhrManager;
        this.tableExtractor = tableExtractor;
        this.cellReader = cellReader;
        this.debugDrawer = debugDrawer;
    }

    public void start() {

        openEhrManager.checkForTemplate("Nephro_Medikation");

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


                    CellReader.ReadingResult result = cellReader.processPage(page, table);
                    if (result.hasTable()) {
                        print2D(result.table());
                        openEhrManager.sendNephroMedikationData(result);
                    }
                } else {
                    log.info("No medication table found");
                }
                log.info("Finished page {}, {}%", page, (page + 1) * 100 / numberOfPages);
            }
            cellReader.finish();
            tableExtractor.finish();
            log.info("Finished file {}, {}%", file.getName(), (i + 1) * 100 / files.length);
        }
    }

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
}
