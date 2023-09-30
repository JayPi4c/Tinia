package com.jaypi4c.ba.pipeline.medicationplan.recognition;

import com.jaypi4c.ba.pipeline.medicationplan.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CellReader {

    @Value("${pdf.dpi}")
    private int DPI;
    private static final String[] DEFAULT_HEADER = WordUtils.loadDictionary("/dictionaries/HeaderAllowlist.txt");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.\\d{2,4}");

    private boolean documentClosed = true;
    private PDDocument document;


    public void setPdfFile(File pdfFile) {
        if (!documentClosed)
            finish();
        try {
            document = PDDocument.load(pdfFile);
            documentClosed = false;
        } catch (IOException e) {
            log.error("Error while loading pdf", e);
        }
    }

    /**
     * Closes the pdf document
     */
    public void finish() {
        try {
            document.close();
            documentClosed = true;
        } catch (IOException e) {
            log.error("Failed to close document: ", e);
        }
    }

    /**
     * 72 points per inch
     * points = pixels * 72 / DPI
     */
    private static int pixelsToPoints(int pixelVal, int dpi) {
        return pixelVal * 72 / dpi;
    }

    public Optional<String[][]> processPage(int page, Rectangle2D[][] table) {
        if (documentClosed) {
            log.error("Document is closed. Probably the file was not set.");
            return Optional.empty();
        }

        String[][] results = new String[table.length][table[0].length];
        PDPage docPage = document.getPage(page);

        try {

            // extract print date from page
            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(page + 1);
            stripper.setEndPage(page + 1);
            String text = stripper.getText(document);
            Matcher matcher = DATE_PATTERN.matcher(text);
            String date = null;
            if (matcher.find()) {
                date = matcher.group(0);
                log.info("Found date: {}", date);
            } else {
                log.info("No date found.");
            }


            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    Rectangle2D rect = transformToPDFRectangle(table, i, j);

                    textStripper.addRegion(i + "_" + j, rect);
                }
            }


            textStripper.extractRegions(docPage);

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    String textForRegion = textStripper.getTextForRegion(i + "_" + j);
                    textForRegion = textForRegion.replaceAll("\n", " ").replaceAll("\r", " ");
                    results[i][j] = textForRegion;
                }
            }
        } catch (IOException e) {
            log.error("Error while reading pdf", e);
        }
        results = verifyTable(results);
        return Optional.ofNullable(results);
    }

    private String[][] verifyTable(String[][] table) {
        int rowIndex = 0;
        // first row should be the header. Let's check if some Words are in there:

        List<String[]> result = new ArrayList<>();
        String[] header = table[0];

        if (!verifyHeader(header)) {
            log.debug("Table has no Header. Inserting a Default Header...");
            result.add(DEFAULT_HEADER);
        } else {
            log.debug("Found header in table");
            result.add(header);
            rowIndex++;
        }
        for (; rowIndex < table.length; rowIndex++) {
            String[] row = table[rowIndex];
            if (verifyRow(row)) {
                result.add(row);
            }
        }
        if (result.size() < 2) {
            log.debug("Table has no rows. Returning empty table");
            return null;
        }
        return result.toArray(new String[0][0]);
    }

    private boolean verifyHeader(String[] header) {
        int totalDistance = 0;
        int totalMatches = 0;
        for (String word : header) {
            WordUtils.LDResult result = WordUtils.findClosestWord(word.trim(), DEFAULT_HEADER);
            totalDistance += result.distance();
            if (result.exactMatch())
                totalMatches++;
        }
        return totalMatches >= 4 || (totalDistance < 15);
    }

    private boolean verifyRow(String[] row) {
        if (row.length != 11) {
            return false;
        }
        String rowString = String.join(" ", row);
        if (rowString.replaceAll(" ", "").length() < 10) {
            return false; // less than 10 characters is probably an almost empty row with no information
        }
        return !row[3].isBlank(); // Darreichungsform darf nicht leer sein.
    }


    private Rectangle2D transformToPDFRectangle(Rectangle2D[][] table, int i, int j) {
        Rectangle2D cell = table[i][j];
        int x = pixelsToPoints((int) cell.getX(), DPI);
        int y = pixelsToPoints((int) cell.getY(), DPI);
        int width = pixelsToPoints((int) cell.getWidth(), DPI);
        int height = pixelsToPoints((int) cell.getHeight(), DPI);
        // log.debug("creating rectangle: {}, {}, {}, {}", cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
        return new Rectangle2D.Double(x, y, width, height);
    }

}
