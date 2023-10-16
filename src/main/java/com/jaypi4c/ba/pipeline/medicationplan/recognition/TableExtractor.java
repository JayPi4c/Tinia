package com.jaypi4c.ba.pipeline.medicationplan.recognition;

import com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing.CellIdentifier;
import com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing.ImageUtils;
import com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing.LineExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableExtractor {

    /*
       for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
        If possible use always this order as it's faster:
        https://stackoverflow.com/a/7750416
     */


    private int imageWidth;
    private int imageHeight;
    private PDDocument document;
    private PDFRenderer pdfRenderer;
    private BufferedImage originalImage;

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}[.,]\\d{1,2}[.,]\\d{2,4}");
    private static final String TITLE = "Medikationsplan";


    @Value("${pdf.dpi}")
    private int DPI;

    @Value("${skipWhenNoHeaderFound}")
    private boolean skipWhenNoHeaderFound;

    @Getter
    private Rectangle2D[][] table;
    @Getter
    private String date;

    private final LineExtractor le;
    private final CellIdentifier ci;

    private boolean documentClosed = true;

    public void setCurrentFile(File file) {
        if (!documentClosed) {
            finish();
        }
        try {
            document = PDDocument.load(file);
            pdfRenderer = new PDFRenderer(document);
            documentClosed = false;
        } catch (IOException e) {
            log.error("Error while loading pdf", e);
        }
    }


    /**
     * Starts the execution of the subtasks
     * - load image from pdf page
     * - extracting the Lines
     * - finding the intersections
     * - labeling the intersections and finding the cells
     */
    public void processPage(int page) {
        if (documentClosed) {
            log.error("Document is closed. Probably the file was not set.");
            return;
        }
        date = null;
        table = null;
        try {

            // extract print date from page
            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(page + 1);
            stripper.setEndPage(page + 1);
            String rawPageText = stripper.getText(document);

            // Check if title is present
            if (!rawPageText.contains(TITLE)) {
                log.debug("No title found.");
                if (skipWhenNoHeaderFound) {
                    log.debug("Skipping page because no header was found.");
                    return;
                }
            }

            Matcher matcher = DATE_PATTERN.matcher(rawPageText);

            if (matcher.find()) {
                date = matcher.group(0);
                date = date.replaceAll(",", ".");
                log.debug("Found date: {}", date);
            } else {
                log.debug("No date found.");
            }
        } catch (IOException ex) {
            log.debug("Error while reading pdf", ex);
        }

        // https://stackoverflow.com/a/57724726
        // read pdf
        try {
            // get page as image
            originalImage = pdfRenderer.renderImageWithDPI(page, DPI);
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();
        } catch (Exception e) {
            log.error("Error while reading pdf", e);
        }

        le.execute(originalImage);

        List<Line2D> lines = le.getLines();
        BufferedImage imgInEdit = ImageUtils.createImageWithLines(imageWidth, imageHeight, lines);

        ci.execute(imgInEdit, lines);
        List<Rectangle2D> rawCells = ci.getCells();

        table = createTable(rawCells);
    }


    private Rectangle2D[][] createTable(List<Rectangle2D> cells) {
        List<List<Rectangle2D>> rows = new ArrayList<>();
        // iterate through all cells. If there is a cell in a row with roughly the same y value, add it to the row
        // otherwise create a new row
        for (Rectangle2D cell : cells) {
            boolean found = false;
            for (List<Rectangle2D> row : rows) { // check existing rows
                if (Math.abs(row.get(0).getY() - cell.getY()) < 10) {
                    row.add(cell);
                    found = true;
                    break;
                }
            }
            if (!found) { // create new row
                List<Rectangle2D> newRow = new ArrayList<>();
                newRow.add(cell);
                rows.add(newRow);
            }
        }

        List<Rectangle2D[]> tableRows = new ArrayList<>();

        final int EXPECTED_NUM_CELLS = 11;
        for (List<Rectangle2D> row : rows) {
            if (row.size() == EXPECTED_NUM_CELLS) {
                row.sort((o1, o2) -> (int) (o1.getX() - o2.getX()));
                tableRows.add(row.toArray(new Rectangle2D[0]));
            } else if (Math.abs(row.size() - EXPECTED_NUM_CELLS) > 3) {
                continue; // skip the row as there are too many or too few cells
            } else {
                // TODO find missing cells
            }

        }

        return tableRows.toArray(new Rectangle2D[0][0]);
    }

    public boolean wasSuccessful() {
        return table != null && table.length > 0;
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
     * Not in use. Maybe this will be part in Image preprocessing in the future.
     *
     * @param inputImage     the image to increase the contrast of
     * @param contrastFactor the factor to increase the contrast by
     * @return the image with increased contrast
     */
    public static BufferedImage increaseContrast(BufferedImage inputImage, double contrastFactor) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Apply contrast enhancement to each channel
                r = (int) (r * contrastFactor);
                g = (int) (g * contrastFactor);
                b = (int) (b * contrastFactor);

                // Clip values to 0-255 range
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                int enhancedRgb = (r << 16) | (g << 8) | b;
                outputImage.setRGB(x, y, enhancedRgb);
            }
        }

        return outputImage;
    }

}
