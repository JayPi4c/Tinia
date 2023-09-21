package com.jaypi4c;

import com.jaypi4c.openehr.OpenEhrManager;

import com.jaypi4c.recognition.CellReader;
import com.jaypi4c.recognition.TableExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        log.info("Starting application");

        String inputFolder = System.getenv("inputFolder");
        inputFolder = inputFolder == null ? "/home/jonas/Studium/cloud/BA/BA Daten/" : inputFolder;

        File[] files = getFiles(inputFolder);

        // files = new File[]{new File("/home/jonas/Studium/cloud/BA/BA Daten/" + "SF_20220104_50335_HA1_LETTER.pdf")};

        OpenEhrManager openEhrManager = new OpenEhrManager();

        for (File file : files) {
            int numberOfPages = getNumberOfPages(file);
            for (int page = 0; page < numberOfPages; page++) {
                TableExtractor tableExtractor = new TableExtractor(file, page);

                tableExtractor.start();
                tableExtractor.finish();

                if (tableExtractor.wasSuccessful()) {
                    Rectangle2D[][] table = tableExtractor.getTable();
                    CellReader cr = new CellReader(file, page, table);
                    String[][] results = cr.readArea();

                    print2D(results);

                    openEhrManager.sendNephroMedikationData(results);

                } else {
                    log.info("No medication table found");
                }
                log.info("Finished page {}", page);
            }
            log.info("Finished file {}", file.getName());
        }
        log.info("Finished application");
    }

    private static File[] getFiles(String path) {
        File folder = new File(path);
        return Arrays.stream(folder.listFiles())
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
