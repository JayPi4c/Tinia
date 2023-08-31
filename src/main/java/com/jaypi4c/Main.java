package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        log.info("Starting application");

        String pdfPath = "/home/jonas/Studium/cloud/BA/BA Daten/";
        String file1 = "SF_20220104_50335_HA1_LETTER.pdf";
        String file2 = "SF_20220412_50061_HA1_LETTER.pdf";
        String file3 = "SF_20220511_50091_HA1_LETTER.pdf";
        String file4 = "SF_20220620_50193_HA1_LETTER.pdf";

        pdfPath += file4;

        int numberOfPages = getNumberOfPages(pdfPath);
        for (int i = 0; i < numberOfPages; i++) {

            TableExtractor tableExtractor = new TableExtractor(pdfPath, i);

            tableExtractor.start();

            List<Rectangle2D> cells = tableExtractor.getCells();
            tableExtractor.finish();
            if (!cells.isEmpty()) {
                CellReader cr = new CellReader(pdfPath, i, cells);
                String[] results = cr.readArea();
                for (String result : results) {
                    log.debug(result);
                }
            } else {
                log.info("No cells found");
            }
            log.info("Finished page {}", i);
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
}
