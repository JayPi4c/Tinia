package com.jaypi4c.recognition;

import com.jaypi4c.recognition.preprocessing.CellIdentifier;
import com.jaypi4c.recognition.preprocessing.ImageUtils;
import com.jaypi4c.recognition.preprocessing.LineExtractor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.geom.Line2D;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
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
    private BufferedImage originalImage;

    @Getter
    private List<Rectangle2D> cells;


    /**
     * Creates the table extractor and loads the pdf from the path
     *
     * @param in path to file pdf file to read
     */
    public TableExtractor(String in, int pageIndex) {
        //https://stackoverflow.com/a/57724726
        // read pdf
        try {
            document = PDDocument.load(new File(in));
            PDFRenderer pr = new PDFRenderer(document);
            // get page as image
            originalImage = pr.renderImageWithDPI(pageIndex, 300);
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();
        } catch (Exception e) {
            log.error("Error while reading pdf", e);
        }
    }


    /**
     * Starts the execution of the subtasks
     * - extracting the Lines
     * - finding the intersections
     * - labeling the intersections and finding the cells
     */
    public void start() {
        LineExtractor le = new LineExtractor(originalImage);
        le.execute();

        List<Line2D> lines = le.getLines();
        BufferedImage imgInEdit = ImageUtils.createImageWithLines(imageWidth, imageHeight, lines);


        CellIdentifier ci = new CellIdentifier(imgInEdit, lines);
        ci.execute();
        cells = ci.getCells();
    }

    /**
     * Closes the pdf document and saves the debug image
     */
    public void finish() {
        try {
            document.close();
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
