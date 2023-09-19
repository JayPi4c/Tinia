package com.jaypi4c.recognition;

import com.jaypi4c.recognition.preprocessing.ImageUtils;
import com.jaypi4c.recognition.preprocessing.LineExtractor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TableExtractor {

    /*
       for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
        If possible use always this order as it's faster:
        https://stackoverflow.com/a/7750416
     */


    private List<Line2D> lines;
    private List<Point2D.Float> intersections;
    private int imageWidth;
    private int imageHeight;
    private PDDocument document;
    private BufferedImage originalImage;
    private BufferedImage imgInEdit;

    private byte[][] nodeMatrix;

    private int pageIndex;

    /**
     * May only be filled after calling start()
     */
    @Getter
    private final List<Rectangle2D> cells = new ArrayList<>();


    // debug
    private final String imageSavePath = "/home/jonas/Studium/cloud/BA/BA Daten/out.jpg";
    private BufferedImage debugImage;


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
            this.pageIndex = pageIndex;
            // get page as image
            originalImage = pr.renderImageWithDPI(pageIndex, 300);
            imgInEdit = ImageUtils.deepCopy(originalImage);
            imageWidth = originalImage.getWidth();
            imageHeight = originalImage.getHeight();
            nodeMatrix = new byte[imageWidth][imageHeight];
        } catch (Exception e) {
            log.error("Error while reading pdf", e);
        }
    }

    /**
     * Closes the pdf document and saves the debug image
     */
    public void finish() {
        try {
            document.close();
            ImageIO.write(debugImage, "JPEG", new File(imageSavePath));
        } catch (IOException e) {
            log.error("Failed to close document: ", e);
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

        lines = le.getLines();
        imgInEdit = ImageUtils.createImageWithLines(imageWidth, imageHeight, lines);

        findIntersections();
        labelIntersections();

        // Find the corresponding Cell for each intersection

        for (Point2D.Float intersection : intersections)
            findCellForIntersection(intersection);
    }


    /**
     * Checks all the lines that are in the list for intersections and adds them to the intersections list.
     */
    private void findIntersections() {
        intersections = new ArrayList<>();
        // check for intersections
        log.info("Checking for intersections...");
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                Line2D line1 = lines.get(i);
                Line2D line2 = lines.get(j);
                if (line1.intersectsLine(line2)) {
                    // they intersect -> find the intersection point:
                    Point2D.Float interceptionPoint = calculateInterceptionPoint(line1, line2);

                    // check if point is within 5 pixels of another point in the list.
                    // if it is, it is probably the same intersection and should not be added again.
                    boolean found = false;

                    for (Point2D.Float p : intersections) {
                        if (p.distance(interceptionPoint) < 5) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        intersections.add(interceptionPoint);
                }
            }
        }


        // debug draw blue intersections
        /*int len = 3;
        for (Point2D.Float p : intersections) {
            for (int x = (int) p.x - len; x <= p.x + len; x++) {
                for (int y = (int) p.y - len; y <= p.y + len; y++) {
                    input.setRGB(x, y, Color.BLUE.getRGB());
                }
            }
        }*/

    }


    /**
     * Following the information from the paper, the intersections are labeled with a number between 0 and 9. Information
     * on the meaning of the numbers can be found in the paper. (Document Recognition and XML Generation of Tabular Form
     * Discharge Summaries for Analogous Case Search System)
     * <br>
     * TODO: Link paper
     */
    private void labelIntersections() {

        for (Point2D intersection : intersections) {
            // check if above, below, left and / or is a line
            // using a four bit number the position of the bit can hold the information of the position of the line
            // using the masking it does not matter how often a line is detected above as it will only set the first
            // bit again and again but does not change the other bits
            byte b = 0b0000;
            int intersectionOffset = 10;

            Point2D pointAbove = new Point2D.Double(intersection.getX(), intersection.getY() - intersectionOffset);
            Point2D pointBelow = new Point2D.Double(intersection.getX(), intersection.getY() + intersectionOffset);
            Point2D pointLeft = new Point2D.Double(intersection.getX() - intersectionOffset, intersection.getY());
            Point2D pointRight = new Point2D.Double(intersection.getX() + intersectionOffset, intersection.getY());


            if (ImageUtils.checkAreaAroundForBlackPixel(imgInEdit, pointAbove, 3)) {
                b = (byte) (b | 0b0001);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(imgInEdit, pointBelow, 3)) {
                b = (byte) (b | 0b0010);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(imgInEdit, pointLeft, 3)) {
                b = (byte) (b | 0b0100);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(imgInEdit, pointRight, 3)) {
                b = (byte) (b | 0b1000);
            }

            log.debug("b: {}", b);
            // b is now a number between 0 and 15
            // 0  = 0000 = no line
            // 1  = 0001 = above
            // 2  = 0010 = below
            // 3  = 0011 = above and below
            // 4  = 0100 = left
            // 5  = 0101 = above and left
            // 6  = 0110 = below and left
            // 7  = 0111 = above, below and left
            // 8  = 1000 = right
            // 9  = 1001 = above and right
            // 10 = 1010 = below and right
            // 11 = 1011 = above, below and right
            // 12 = 1100 = left and right
            // 13 = 1101 = above, left and right
            // 14 = 1110 = below, left and right
            // 15 = 1111 = above, below, left and right

            // translate to the numbers from the paper and store it into the nodeMatrix
            switch (b) {
                case 15 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 5;
                case 14 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 2;
                case 13 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 8;
                case 11 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 4;
                case 10 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 1;
                case 9 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 7;
                case 7 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 6;
                case 6 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 3;
                case 5 -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 9;
                default -> nodeMatrix[(int) intersection.getX()][(int) intersection.getY()] = 0;
            }
        }
        debugImage = ImageUtils.deepCopy(imgInEdit);

        // debug: draw number for node
        for (Point2D intersection : intersections) {
            int x = (int) intersection.getX();
            int y = (int) intersection.getY();
            debugImage.setRGB(x, y, Color.GREEN.getRGB());
            // write String into image
            Graphics g = debugImage.getGraphics();
            g.setColor(Color.RED);
            g.drawString(String.valueOf(nodeMatrix[x][y]), x, y);
        }

    }

    /**
     * Finds the cell for the given intersection and adds it to the cells list.
     * <br>
     * The intersection must have one of the labels 1, 2, 4 or 5. If it does not, the method will return without doing anything.
     * These labels signal that the intersection can be a top left corner of a cell and the method will try to find the
     * top right and bottom left corner of the cell to create a rectangle.
     * <br>
     * Future implementation may also check for the bottom right corner to validate the cell.
     *
     * @param intersection the intersection to find the cell for
     */
    private void findCellForIntersection(Point2D intersection) {

        byte node = nodeMatrix[(int) intersection.getX()][(int) intersection.getY()];
        if (node != 1 && node != 2 && node != 4 && node != 5) {
            log.info("intersection not valid for cell top left");
            return;
        }


        Point2D.Float topRight = null;
        Point2D.Float bottomLeft = null;
        Point2D.Float bottomRight = null;

        for (Point2D.Float other : intersections) {
            if (other == intersection) {// don't check with yourself
                continue;
            }

            //----------- check for top right node ----------------

            // check if y is within plus minus 5 pixels
            if (other.getY() > intersection.getY() - 5 && other.getY() < intersection.getY() + 5) {
                // check if intersection is of type 2, 3, 5, 6 (top right)
                byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
                if (otherNode == 2 || otherNode == 3 || otherNode == 5 || otherNode == 6) {
                    // check if x is closer than previous best for topRight
                    if (other.getX() > intersection.getX() && (topRight == null || other.getX() < topRight.getX())) {
                        // then use it!
                        topRight = other;
                    }
                }
            }

            //----------- check for bottom left node ----------------

            // check if x is within plus minus 5 pixels

            if (other.getX() > intersection.getX() - 5 && other.getX() < intersection.getX() + 5) {
                // check if intersection is of type 4, 5, 7, 8 (bottom left)
                byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
                if (otherNode == 4 || otherNode == 5 || otherNode == 7 || otherNode == 8) {
                    // check if y is closer than previous best for bottomLeft
                    if (other.getY() > intersection.getY() && (bottomLeft == null || other.getY() < bottomLeft.getY())) {
                        // then use it!
                        bottomLeft = other;
                    }
                }
            }

            //---------------- check for bottom right node ----------------

            // it's a candidate if it's of type 5, 6, 8 or 9
            byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
            if (otherNode == 5 || otherNode == 6 || otherNode == 8 || otherNode == 9) {
                // check if x is more than 5 pixels greater than intersection
                if (other.getX() > intersection.getX() + 15) {
                    // check if y is more than 5 pixels greater than intersection
                    if (other.getY() > intersection.getY() + 15) {
                        // check if x and y are closer or equal (+-5 pixels)to the previous best for bottomRight
                        if (bottomRight == null) {
                            bottomRight = other;
                        } else if (other.getX() <= bottomRight.getX() + 5 && other.getY() <= bottomRight.getY() + 5) {
                            bottomRight = other;
                        }
                    }
                }
            }

        }
        if (topRight == null || bottomLeft == null || bottomRight == null) {
            log.debug("no cell found for intersection: {}", intersection);
            return;
        }


        // Add the cell to the list and color it in the debug image
        Color color = ImageUtils.randomColor();
        log.debug("found cell: topRight: {}, bottomLeft: {}, bottomRight {}", topRight, bottomLeft, bottomRight);
        Rectangle2D.Double cell = new Rectangle2D.Double(intersection.getX(), intersection.getY(), Math.min(topRight.getX(), bottomRight.getX()) - intersection.getX(), Math.min(bottomLeft.getY(), bottomRight.getY()) - intersection.getY());
        cells.add(cell);
        for (int x = (int) intersection.getX(); x <= intersection.getX() + cell.getWidth(); x++) {
            for (int y = (int) intersection.getY(); y <= intersection.getY() + cell.getHeight(); y++) {
                debugImage.setRGB(x, y, color.getRGB());
            }
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

    //-----------------------------------------------
    //------------------ HELPER ---------------------
    //-----------------------------------------------

    /**
     * @param line1 the first line
     * @param line2 the second line
     * @return the intersection point of the two lines
     * @see <a href=https://stackoverflow.com/a/61574355>Stackoverflow</a>
     */
    public static Point2D.Float calculateInterceptionPoint(Line2D line1, Line2D line2) {

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


}
