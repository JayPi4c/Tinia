package com.jaypi4c.recognition;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class CellReader {

    private final String pdfPath;
    private final Rectangle2D[][] table;
    final int DPI = 300;
    private int pageIndex;

    public CellReader(String pdfPath, int pageIndex, Rectangle2D[][] table) {
        this.pdfPath = pdfPath;
        this.table = table;
        this.pageIndex = pageIndex;
    }

    /**
     * 72 points per inch
     * points = pixels * 72 / DPI
     */
    private static int pixelsToPoints(int pixelVal, int dpi) {
        return pixelVal * 72 / dpi;
    }

    public String[][] readArea() throws Exception {

        String[][] results = new String[table.length][table[0].length];

        try (PDDocument pd = PDDocument.load(new File(pdfPath))) {

            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    Rectangle2D rect = transformToPDFRectangle(table, i, j);

                    textStripper.addRegion(i + "_" + j, rect);
                }
            }
            PDPage docPage = pd.getPage(pageIndex);

            textStripper.extractRegions(docPage);

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    String textForRegion = textStripper.getTextForRegion(i + "_" + j);
                    textForRegion = textForRegion.replaceAll("\n", " ").replaceAll("\r", " ");
                    results[i][j] = textForRegion;
                }
            }


        } catch (Exception e) {
            log.error("Error while reading pdf: " + e.getMessage());
            throw e;
        }
        return results;
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
