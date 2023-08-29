package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.List;

@Slf4j
public class CellReader {

    private final String pdfPath;
    private final List<Rectangle2D> cells;
    final int DPI = 300;

    public CellReader(String pdfPath, List<Rectangle2D> cells) {
        this.pdfPath = pdfPath;
        this.cells = cells;
    }

    /**
     * 72 points per inch
     * points = pixels * 72 / DPI
     */
    private static int pixelsToPoints(int pixelVal, int dpi) {
        return pixelVal * 72 / dpi;
    }

    public String[] readArea() throws Exception {
        String[] results = new String[cells.size()];


        int page = 0;

        try (PDDocument pd = PDDocument.load(new File(pdfPath))) {

            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();


            for (int i = 0; i < cells.size(); i++) {
                Rectangle2D cell = cells.get(i);
                int x = pixelsToPoints((int) cell.getX(), DPI);
                int y = pixelsToPoints((int) cell.getY(), DPI);
                int width = pixelsToPoints((int) cell.getWidth(), DPI);
                int height = pixelsToPoints((int) cell.getHeight(), DPI);
                log.info("creating rectangle: {}, {}, {}, {}", cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);

                textStripper.addRegion("cell" + i, rect);

            }
            PDPage docPage = pd.getPage(page);

            textStripper.extractRegions(docPage);

            for (int i = 0; i < cells.size(); i++) {
                String textForRegion = textStripper.getTextForRegion("cell" + i);
                results[i] = textForRegion;
            }


        } catch (Exception e) {
            log.error("Error while reading pdf: " + e.getMessage());
            throw e;
        }
        return results;
    }

}
