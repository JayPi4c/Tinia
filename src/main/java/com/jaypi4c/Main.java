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
import java.util.List;


@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        log.info("Starting application");

        String pdfPath = "/home/jonas/Studium/cloud/BA/BA Daten/";
        String file1 = "SF_20220104_50335_HA1_LETTER.pdf";
        String file2 = "SF_20220412_50061_HA1_LETTER.pdf";// no medication plan
        String file3 = "SF_20220511_50091_HA1_LETTER.pdf";
        String file4 = "SF_20220620_50193_HA1_LETTER.pdf";
        String file5 = "SF_20220213_50007_HA1_LETTER.pdf";

        pdfPath += file4;

        OpenEhrManager openEhrManager = new OpenEhrManager();


        int numberOfPages = 1;// getNumberOfPages(pdfPath);
        for (int page = 0; page < numberOfPages; page++) {
            // page = 1;
            TableExtractor tableExtractor = new TableExtractor(pdfPath, page);

            tableExtractor.start();
            tableExtractor.finish();

            if (tableExtractor.wasSuccessful()) {
                Rectangle2D[][] table = tableExtractor.getTable();
                CellReader cr = new CellReader(pdfPath, page, table);
                String[][] results = cr.readArea();

                print2D(results);

                // openEhrManager.sendNephroMedikationData(results);

            } else {
                log.info("No cells found");
            }
            log.info("Finished page {}", page);
        }

        log.info("Finished application");
    }

    private static int getNumberOfPages(String in) {
        try (PDDocument document = PDDocument.load(new File(in))) {
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
