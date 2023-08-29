package com.jaypi4c;

import lombok.Getter;
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
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
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


    private List<Line2D.Float> lines;
    private List<Point2D.Float> intersections;
    private int imageWidth;
    private int imageHeight;
    private PDDocument document;
    private BufferedImage originalImage;
    private BufferedImage imgInEdit;

    private byte[][] nodeMatrix;

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
    public TableExtractor(String in) {
        //https://stackoverflow.com/a/57724726
        // read pdf
        try {
            document = PDDocument.load(new File(in));
            PDFRenderer pr = new PDFRenderer(document);
            // get page as image
            originalImage = pr.renderImageWithDPI(0, 300);
            imgInEdit = deepCopy(originalImage);
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
        filterLines();
        findIntersections();
        labelIntersections();
        // Find the corresponding Cell for each intersection
        for (Point2D.Float intersection : intersections)
            findCellForIntersection(intersection);
    }

    /**
     * extracts the lines from the image given in originalImage
     * <br>
     * For it to work, the image may not be skewed.
     */
    private void filterLines() {

        // increase contrast
        // bi = increaseContrast(bi, 1.7);

        log.info("removing the text from the image");
        removeText();

        // connected components labeling to remove black areas
        log.info("removing black areas from the image");
        connectedComponentsLabeling();
    }

    /**
     * Removes the text from the image as it scans for continuous black lines. <br>
     * If n (= 50) pixels are black in a row, it is considered a line. Checking each column and row for lines with this method leaves the images with none of the text.
     * <br>
     * Blackened areas are considered as continuous black lines and therefore remain in the image. Using the connected Components Algorithm on the image can remove those areas.
     */
    private void removeText() {
        // create empty result image
        imgInEdit = createWhiteBackgroundImage(imageWidth, imageHeight);

        // we want to store all lines and be able to look them up at any time.
        lines = new ArrayList<>();


        log.info("Processing horizontal lines...");
        // perform line detection on image
        final int n = 50; // taken from literature
        final int blackThreshold = 180;

        // TODO find efficient way to move horizontally and vertically
        // horizontal lines
        // go through each row and check if there are n consecutive black pixels
        // keep going until there is a white pixel. Then add the line to the list
        for (int y = 0; y < originalImage.getHeight(); y++) {
            int beginX = -1;
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int gray = getGray(originalImage.getRGB(x, y));

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


        log.info("Processing vertical lines...");
        // vertical lines
        // go through each column and check if there are n consecutive black pixels
        // keep going until there is a white pixel. Then add the line to the list
        for (int x = 0; x < originalImage.getWidth(); x++) {
            int beginY = -1;
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int gray = getGray(originalImage.getRGB(x, y));

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

        // draw the found lines onto the white image in edit
        // this image will be used for further processing as it only contains the lines and black areas
        for (Line2D line : lines) {
            for (int x = (int) line.getX1(); x <= line.getX2(); x++) {
                for (int y = (int) line.getY1(); y <= line.getY2(); y++) {
                    imgInEdit.setRGB(x, y, 0x00000000);
                }
            }
        }
    }

    /**
     * ConnectedComponentsAlgorithm to remove black areas from the image
     * <br>
     * https://aishack.in/tutorials/labelling-connected-components-example/
     *
     * <br>
     * As there are only the colors black and white left, the algorithm is straight forward: In the first pass, all
     * black pixels get a label. In the following pass, connected labels will be reduced to on common root label.
     * This leaves connected areas with the same label and the density of black pixels in one connected area can be used
     * to determine if it is a blackened area.
     */
    private void connectedComponentsLabeling() {
        int[][] labels = new int[imageWidth][imageHeight];
        int label = 1;
        int backgroundLabel = 0;

        Map<Integer, Integer> mergeList = new HashMap<>();

        int backgroundThreshold = 200;

        log.info("starting first pass");
        // first pass
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (getGray(imgInEdit.getRGB(x, y)) > backgroundThreshold) {
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
        log.info("starting second pass");
        // second pass
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
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
        // debug: colorize chunks that are considered blackened areas
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

        /* Now the label array can be used to determine the connected chunks. This is done by iterating over the labels array
         * and checking if the label is already in the chunks map. If it is, the chunk is updated/expanded with the new
         * coordinates. Finally, this leaves us with a map of chunks that have all the same label. Checking the black pixel-density
         * will give the information if the chunk is a blackened area or not.
         */
        Map<Integer, Chunk> chunks = new HashMap<>();

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
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
        log.info("remove black chunks");
        for (Chunk c : chunks.values()) {
            long avg = 0;
            for (int x = c.minX(); x <= c.maxX(); x++) {
                for (int y = c.minY(); y <= c.maxY(); y++) {
                    avg += getGray(imgInEdit.getRGB(x, y));
                }
            }
            // if average color is dark the chunk is probably a blackened area -> remove
            // also if the chunk is too small, meaning just a single line -> remove
            // TODO: maybe a single line should stay...
            avg /= (long) (c.maxX() - c.minX() + 1) * (c.maxY() - c.minY() + 1);
            if (avg < 150 || c.maxX() - c.minX() < 20 || c.maxY() - c.minY() < 20) {
                for (int x = c.minX(); x <= c.maxX(); x++) {
                    for (int y = c.minY(); y <= c.maxY(); y++) {
                        imgInEdit.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }

                // remove all lines from lines list if they are in the blackened chunk
                for (int i = 0; i < lines.size(); i++) {
                    Line2D.Float line = lines.get(i);
                    if (c.isWithin(line)) {
                        lines.remove(i);
                        i--;
                    }
                }
            }
        }
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
                Line2D.Float line1 = lines.get(i);
                Line2D.Float line2 = lines.get(j);
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


            if (checkAreaAroundForBlackPixel(imgInEdit, pointAbove, 3)) {
                b = (byte) (b | 0b0001);
            }
            if (checkAreaAroundForBlackPixel(imgInEdit, pointBelow, 3)) {
                b = (byte) (b | 0b0010);
            }
            if (checkAreaAroundForBlackPixel(imgInEdit, pointLeft, 3)) {
                b = (byte) (b | 0b0100);
            }
            if (checkAreaAroundForBlackPixel(imgInEdit, pointRight, 3)) {
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
        debugImage = deepCopy(imgInEdit);

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
     * @param intersection the intersection to find the cell for
     */
    private void findCellForIntersection(Point2D intersection) {

        byte node = nodeMatrix[(int) intersection.getX()][(int) intersection.getY()];
        if (node != 1 && node != 2 && node != 4 && node != 5) {
            log.info("intersection not valid for cell top right");
            return;
        }


        Point2D.Float topRight = null;
        Point2D.Float bottomLeft = null;

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
            // TODO: check for bottom right to validate full cell.

        }

        // Add the cell to the list and color it in the debug image
        Color color = randomColor();
        if (topRight != null && bottomLeft != null) {
            log.info("found cell: topRight: {}, bottomLeft: {}", topRight, bottomLeft);
            cells.add(new Rectangle2D.Double(topRight.getX(), topRight.getY(), bottomLeft.getX() - topRight.getX(), bottomLeft.getY() - topRight.getY()));
            for (int x = (int) intersection.getX(); x <= topRight.getX(); x++) {
                for (int y = (int) intersection.getY(); y <= bottomLeft.getY(); y++) {
                    debugImage.setRGB(x, y, color.getRGB());
                }
            }
        } else {
            log.debug("no cell found for intersection: {}", intersection);
        }


    }

    /**
     * Not in use. Maybe this will be part in Image preprocessing in the future.
     * @param inputImage
     * @param contrastFactor
     * @return
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

    /**
     * 72 points per inch
     * points = pixels * 72 / DPI
     */
    private int pixelsToPoints(int pixelVal, int dpi) {
        return pixelVal * 72 / dpi;
    }


    private boolean checkAreaAroundForBlackPixel(BufferedImage image, Point2D center, int offset) {
        for (double i = center.getX() - offset; i < center.getX() + offset; i++) {
            for (double j = center.getY() - offset; j < center.getY() + offset; j++) {
                if (getGray(image.getRGB((int) i, (int) j)) < 50) {
                    return true;
                }
            }
        }
        return false;
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

    public void readArea(String in) throws Exception {
        final int DPI = 300;

        int page = 0;
        int x = pixelsToPoints(100, DPI);
        int y = pixelsToPoints(647, DPI);
        int width = pixelsToPoints(470, DPI);
        int height = pixelsToPoints(125, DPI);

        PDDocument document = PDDocument.load(new File(in));

        PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
        Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);
        textStripper.addRegion("region", rect);


        PDPage docPage = document.getPage(page);

        textStripper.extractRegions(docPage);

        String textForRegion = textStripper.getTextForRegion("region");

        System.out.println(textForRegion);
    }

    /**
     * https://stackoverflow.com/a/3514297
     *
     * @param bi
     * @return
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
