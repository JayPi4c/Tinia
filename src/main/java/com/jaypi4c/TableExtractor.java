package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TableExtractor {

    /*
       for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
        If possible use always this order as it's faster:
        https://stackoverflow.com/a/7750416
     */


    /**
     * 72 points per inch
     * points = pixels * 72 / DPI
     */
    private int pixelsToPoints(int pixelVal, int dpi) {
        return pixelVal * 72 / dpi;
    }

    public void readArea(String in) throws Exception {
        final int DPI = 300;

        int page = 0;
        int x = pixelsToPoints(100, DPI);
        int y = pixelsToPoints(647, DPI);
        int width = pixelsToPoints(470, DPI);
        int height = pixelsToPoints(125, DPI);

        PDDocument document = PDDocument.load(new File(in));

        PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
        Rectangle2D rect = new java.awt.geom.Rectangle2D.Float(x, y, width, height);
        textStripper.addRegion("region", rect);


        PDPage docPage = document.getPage(page);

        textStripper.extractRegions(docPage);

        String textForRegion = textStripper.getTextForRegion("region");

        System.out.println(textForRegion);
    }

    public void execute(String in, String out) {

        log.info("Extracting table from {} and store result image in {}", in, out);

        //https://stackoverflow.com/a/57724726
        // read pdf
        try (PDDocument pd = PDDocument.load(new File(in))) {
            PDFRenderer pr = new PDFRenderer(pd);
            // get page as image
            BufferedImage bi = pr.renderImageWithDPI(0, 300);

            BufferedImage output = filterLines(bi);


            ImageIO.write(output, "JPEG", new File(out));
        } catch (IOException e) {
            log.error("Error while reading pdf", e);
        }
    }

    private BufferedImage filterLines(BufferedImage bi) {


        // increase contrast
        // bi = increaseContrast(bi, 1.7);

        BufferedImage imgInEdit = removeText(bi);


        // connected components labeling to remove black areas
        // https://aishack.in/tutorials/labelling-connected-components-example/
        BufferedImage output = connectedComponentsLabeling(imgInEdit);

        return output;
    }

    private BufferedImage removeText(BufferedImage input) {
        // create empty result image
        BufferedImage output = createWhiteBackgroundImage(input.getWidth(), input.getHeight());


        List<Line2D.Float> lines = new ArrayList<>();

        // perform line detection on image
        final int n = 50; // taken from literature
        final int blackThreshold = 180;

        // horizontal lines
        for (int y = 0; y < input.getHeight(); y++) {
            int beginX = -1;
            for (int x = 0; x < input.getWidth(); x++) {
                int gray = getGray(input.getRGB(x, y));

                if (gray < blackThreshold) {
                    if (beginX < 0) {
                        beginX = x;
                    }
                } else {
                    if (beginX >= 0 && (x - beginX) > n) {
                        lines.add(new Line2D.Float(beginX, y, x, y));
                    }
                    beginX = -1;
                }
            }
        }


        // vertical lines
        for (int x = 0; x < input.getWidth(); x++) {
            int beginY = -1;
            for (int y = 0; y < input.getHeight(); y++) {
                int gray = getGray(input.getRGB(x, y));

                if (gray < blackThreshold) {
                    if (beginY < 0) {
                        beginY = y;
                    }
                } else {
                    if (beginY >= 0 && (y - beginY) > n) {
                        lines.add(new Line2D.Float(x, beginY, x, y));
                    }
                    beginY = -1;
                }
            }
        }

        // draw lines
        for (Line2D line : lines) {
            for (int x = (int) line.getX1(); x <= line.getX2(); x++) {
                for (int y = (int) line.getY1(); y <= line.getY2(); y++) {
                    output.setRGB(x, y, 0x00000000);
                }
            }
        }
        List<Point2D.Float> intersections = new ArrayList<>();
        // check for intersections
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                Line2D.Float line1 = lines.get(i);
                Line2D.Float line2 = lines.get(j);
                if (line1.intersectsLine(line2)) {
                    // they intersect -> find the intersection point:
                    Point2D.Float interceptionPoint = calculateInterceptionPoint(line1, line2);

                    // check if point +- 2 pixels in x or y is already in the list
                    boolean found = false;

                    for (Point2D.Float p : intersections) {
                        if (Math.abs(p.x - interceptionPoint.x) < 2 && Math.abs(p.y - interceptionPoint.y) < 2) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        intersections.add(interceptionPoint);

                }
            }
        }


        return output;
    }

    /**
     * @param line1
     * @param line2
     * @return
     * @see <a href=https://stackoverflow.com/a/61574355>Stackoverflow</a>
     */
    public static Point2D.Float calculateInterceptionPoint(Line2D.Float line1, Line2D.Float line2) {

        Point2D.Float s1 = (Point2D.Float) line1.getP1();
        Point2D.Float s2 = (Point2D.Float) line1.getP2();
        Point2D.Float d1 = (Point2D.Float) line2.getP1();
        Point2D.Float d2 = (Point2D.Float) line2.getP2();

        double a1 = s2.y - s1.y;
        double b1 = s1.x - s2.x;
        double c1 = a1 * s1.x + b1 * s1.y;

        double a2 = d2.y - d1.y;
        double b2 = d1.x - d2.x;
        double c2 = a2 * d1.x + b2 * d1.y;

        double delta = a1 * b2 - a2 * b1;
        return new Point2D.Float((float) ((b2 * c1 - b1 * c2) / delta), (float) ((a1 * c2 - a2 * c1) / delta));

    }


    private BufferedImage connectedComponentsLabeling(BufferedImage input) {
        int[][] labels = new int[input.getWidth()][input.getHeight()];
        int label = 1;
        int backgroundLabel = 0;

        Map<Integer, Integer> mergeList = new HashMap<>();

        int backgroundThreshold = 200;

        // first pass
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                if (getGray(input.getRGB(x, y)) > backgroundThreshold) {
                    // it's a background pixel -> skip
                    // setting to background label is not needed as default value is 0
                    continue;
                }
                int labelAbove = y > 0 ? labels[x][y - 1] : backgroundLabel; // 0 (background) if out of bounds
                int labelLeft = x > 0 ? labels[x - 1][y] : backgroundLabel; // 0 (background) if out of bounds
                if (labelAbove == backgroundLabel && labelLeft == backgroundLabel) {
                    // new label
                    labels[x][y] = label;
                    label++;
                } else if (labelAbove != backgroundLabel && labelLeft == backgroundLabel) {
                    // label above
                    labels[x][y] = labelAbove;
                } else if (labelAbove == backgroundLabel && labelLeft != backgroundLabel) {
                    // label left
                    labels[x][y] = labelLeft;
                } else if (labelAbove != backgroundLabel && labelLeft != backgroundLabel) {
                    // merge labels
                    labels[x][y] = labelLeft;
                    if (labelLeft != labelAbove && !mergeList.containsKey(labelLeft)) {
                        // add to merge list
                        mergeList.put(labelLeft, labelAbove);
                    }
                }

            }
        }

        log.debug("merge list: {}", mergeList);

        // second pass
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int l = labels[x][y];
                if (l == backgroundLabel) {
                    // it's a background pixel -> skip
                    continue;
                }
                while (mergeList.containsKey(l)) {
                    l = mergeList.get(l);
                }
                labels[x][y] = l;
            }
        }
        /*
        // debug colorize
        Map<Byte, Color> colors = new HashMap<>();
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                byte l = labels[x][y];
                if (l == backgroundLabel) {
                    continue;
                }
                if (!colors.containsKey(l)) {
                    colors.put(l, randomColor());
                }
                input.setRGB(x, y, colors.get(l).getRGB());
            }
        }
        */

        Map<Integer, Chunk> chunks = new HashMap<>();

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int l = labels[x][y];
                if (l == backgroundLabel) {
                    continue;
                }
                if (chunks.containsKey(l)) {
                    Chunk chunk = chunks.get(l);
                    chunk = new Chunk(Math.min(chunk.minX(), x), Math.min(chunk.minY(), y), Math.max(chunk.maxX(), x), Math.max(chunk.maxY(), y));
                    chunks.put(l, chunk);
                } else {
                    chunks.put(l, new Chunk(x, y, x, y));
                }
            }
        }
        for (Chunk c : chunks.values()) {
            long avg = 0;
            for (int x = c.minX(); x <= c.maxX(); x++) {
                for (int y = c.minY(); y <= c.maxY(); y++) {
                    avg += getGray(input.getRGB(x, y));

                }
            }
            // if average color is dark the chunk is probably a blackened area -> remove
            // also if the chunk is too small -> remove
            avg /= (long) (c.maxX() - c.minX() + 1) * (c.maxY() - c.minY() + 1);
            if (avg < 150 || c.maxX() - c.minX() < 20 || c.maxY() - c.minY() < 20) {
                for (int x = c.minX(); x <= c.maxX(); x++) {
                    for (int y = c.minY(); y <= c.maxY(); y++) {
                        input.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }
        }


        return input;
    }

    private Color randomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }

    private BufferedImage createWhiteBackgroundImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Fill the entire image with white color
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        // Dispose the graphics context to free resources
        graphics.dispose();

        return image;
    }


    static int getGray(int rgb) {
        int red = (rgb & 0x00ff0000) >> 16;
        int green = (rgb & 0x0000ff00) >> 8;
        int blue = rgb & 0x000000ff;
        return (red + green + blue) / 3;
    }

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
