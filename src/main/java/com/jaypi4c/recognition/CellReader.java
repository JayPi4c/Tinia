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
    private final List<Rectangle2D> cells;
    final int DPI = 300;
    private int pageIndex;

    public CellReader(String pdfPath, int pageIndex, List<Rectangle2D> cells) {
        this.pdfPath = pdfPath;
        this.cells = cells;
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

        // group the cells by the y coordinate. Different y coordinates within +-10 Pixels are considered to be the same row
        List<List<Rectangle2D>> rows = new ArrayList<>();
        for (Rectangle2D cell : cells) {
            boolean found = false;
            for (List<Rectangle2D> row : rows) {
                if (Math.abs(row.get(0).getY() - cell.getY()) < 10) {
                    row.add(cell);
                    found = true;
                    break;
                }
            }
            if (!found) {
                List<Rectangle2D> newRow = new ArrayList<>();
                newRow.add(cell);
                rows.add(newRow);
            }
        }
        Optional<List<Rectangle2D>> longestRowOpt = rows.stream().reduce((a, b) -> a.size() > b.size() ? a : b);
        if (longestRowOpt.isEmpty()) {
            throw new Exception("Failed to determine the longest row");
        }
        int numEntries = longestRowOpt.get().size();

        String[][] results = new String[rows.size()][numEntries];

        try (PDDocument pd = PDDocument.load(new File(pdfPath))) {

            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();

            for (int i = 0; i < rows.size(); i++) {
                for (int j = 0; j < rows.get(i).size(); j++) {
                    Rectangle2D cell = rows.get(i).get(j);
                    int x = pixelsToPoints((int) cell.getX(), DPI);
                    int y = pixelsToPoints((int) cell.getY(), DPI);
                    int width = pixelsToPoints((int) cell.getWidth(), DPI);
                    int height = pixelsToPoints((int) cell.getHeight(), DPI);
                    // log.debug("creating rectangle: {}, {}, {}, {}", cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                    Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);

                    textStripper.addRegion(i + "_" + j, rect);
                }
            }
            PDPage docPage = pd.getPage(pageIndex);

            textStripper.extractRegions(docPage);

            for (int i = 0; i < rows.size(); i++) {
                for (int j = 0; j < rows.get(i).size(); j++) {
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

}
