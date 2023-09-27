package com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing;

import com.jaypi4c.ba.pipeline.medicationplan.utils.DebugDrawer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CellIdentifier {

    private BufferedImage image;
    private List<Line2D> lines;

    private byte[][] nodeMatrix;

    private List<Point2D> intersections;

    private final DebugDrawer debugDrawer;

    @Getter
    private List<Rectangle2D> cells;


    @Autowired
    public CellIdentifier(DebugDrawer debugDrawer) {
        this.debugDrawer = debugDrawer;
    }

    public void execute(BufferedImage img, List<Line2D> ls) {
        log.info("Starting CellIdentifier");

        image = img;
        lines = ls;
        cells = new ArrayList<>();

        log.info("Searching intersections...");
        findIntersections();

        log.info("Labeling intersections...");
        labelIntersections();

        log.info("Searching for cells");
        identifyCells();

        log.info("Found {} cells", cells.size());
        drawCells();
    }

    private void drawCells() {

        BufferedImage image = ImageUtils.deepCopy(this.image);
        for (Rectangle2D cell : cells) {
            Color color = ImageUtils.randomColor();
            for (int x = (int) cell.getX(); x < cell.getX() + cell.getWidth(); x++) {
                for (int y = (int) cell.getY(); y < cell.getY() + cell.getHeight(); y++) {
                    if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
                        continue;
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }
        debugDrawer.saveDebugImage(image, "cells");
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
                    Point2D interceptionPoint = calculateInterceptionPoint(line1, line2);

                    // check if point is within 5 pixels of another point in the list.
                    // if it is, it is probably the same intersection and should not be added again.
                    boolean found = false;

                    for (Point2D p : intersections) {
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

    }

    /**
     * Following the information from the paper, the intersections are labeled with a number between 0 and 9. Information
     * on the meaning of the numbers can be found in the paper. (Document Recognition and XML Generation of Tabular Form
     * Discharge Summaries for Analogous Case Search System)
     * <br>
     * TODO: Link paper
     */
    private void labelIntersections() {
        nodeMatrix = new byte[image.getWidth()][image.getHeight()];

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


            if (ImageUtils.checkAreaAroundForBlackPixel(image, pointAbove, 3)) {
                b = (byte) (b | 0b0001);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(image, pointBelow, 3)) {
                b = (byte) (b | 0b0010);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(image, pointLeft, 3)) {
                b = (byte) (b | 0b0100);
            }
            if (ImageUtils.checkAreaAroundForBlackPixel(image, pointRight, 3)) {
                b = (byte) (b | 0b1000);
            }

            // log.debug("b: {}", b);

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

        BufferedImage debugImage = ImageUtils.deepCopy(image);
        // debug: draw number for node
        for (Point2D intersection : intersections) {
            int x = (int) intersection.getX();
            int y = (int) intersection.getY();
            // write String into image
            Graphics g = debugImage.getGraphics();
            g.setColor(Color.RED);
            g.drawString(String.valueOf(nodeMatrix[x][y]), x, y);
        }
        debugDrawer.saveDebugImage(debugImage, "labels");

        log.debug("Found {} intersections", intersections.size());
    }

    private void identifyCells() {
        // Find the corresponding Cell for each intersection
        for (Point2D intersection : intersections) {
            Optional<Rectangle2D> cellOpt = findCellForTopLeftCorner(intersection);
            cellOpt.ifPresent(cells::add);
            cellOpt = findCellForBottomLeftCorner(intersection);
            cellOpt.ifPresent(cells::add);
        }
        removeDuplicates();
    }

    private void removeDuplicates() {
        for (int i = cells.size() - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                Rectangle2D cell = cells.get(i);
                Rectangle2D other = cells.get(j);
                if (overlaps(cell, other)) {
                    cells.remove(i);
                    break;
                }
            }
        }
    }

    private boolean overlaps(Rectangle2D cell, Rectangle2D other) {
        Rectangle2D intersection = cell.createIntersection(other);
        if (intersection.getWidth() < 0 || intersection.getHeight() < 0)
            return false; // negative length means no overlap
        double intersectionArea = intersection.getWidth() * intersection.getHeight();
        double cellArea = cell.getWidth() * cell.getHeight();
        return (intersectionArea / cellArea) > 0.5;
    }


    /**
     * Finds the cell for the given intersection and adds it to the cells list.
     * <br>
     * The intersection must have one of the labels 1, 2, 4 or 5. If it does not, the method will return without doing anything.
     * These labels signal that the intersection can be a top left corner of a cell and the method will try to find the
     * top right and bottom left corner of the cell to create a rectangle.
     * <br>
     * TODO Future implementation may also check for the bottom right corner to validate the cell.
     *
     * @param intersection the intersection to find the cell for
     */
    private Optional<Rectangle2D> findCellForTopLeftCorner(Point2D intersection) {

        byte node = nodeMatrix[(int) intersection.getX()][(int) intersection.getY()];
        if (node != 1 && node != 2 && node != 4 && node != 5) {
            return Optional.empty();
        }


        Point2D topRight = null;
        Point2D bottomLeft = null;
        Point2D bottomRight = null;

        for (Point2D other : intersections) {
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
            // log.debug("no cell found for intersection: {}", intersection);
            return Optional.empty();
        }


        // Add the cell to the list and color it in the debug image
        // log.debug("found cell: topRight: {}, bottomLeft: {}, bottomRight {}", topRight, bottomLeft, bottomRight);
        return Optional.of(
                new Rectangle2D.Double(
                        intersection.getX(),
                        intersection.getY(),
                        Math.min(topRight.getX(), bottomRight.getX()) - intersection.getX(),
                        Math.min(bottomLeft.getY(), bottomRight.getY()) - intersection.getY()));
    }


    /**
     * Finds the cell for the given intersection and adds it to the cells list.
     * <br>
     * The intersection must have one of the labels 4, 5, 7 or 8. If it does not, the method will return without doing anything.
     * These labels signal that the intersection can be a bottom left corner of a cell and the method will try to find the
     * bottom right and top left corner of the cell to create a rectangle.
     * <br>
     * TODO Future implementation may also check for the top right corner to validate the cell.
     *
     * @param intersection the intersection to find the cell for
     */
    private Optional<Rectangle2D> findCellForBottomLeftCorner(Point2D intersection) {

        byte node = nodeMatrix[(int) intersection.getX()][(int) intersection.getY()];
        if (node != 4 && node != 5 && node != 7 && node != 8) {
            return Optional.empty();
        }


        Point2D topRight = null;
        Point2D topLeft = null;
        Point2D bottomRight = null;

        for (Point2D other : intersections) {
            if (other == intersection) {// don't check with yourself
                continue;
            }

            //----------- check for bottom right node ----------------

            // check if y is within plus minus 5 pixels
            if (Math.abs(other.getY() - intersection.getY()) <= 5) {
                // check if intersection is of type 5, 6, 8 or 9 (bottom right)
                byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
                if (otherNode == 5 || otherNode == 6 || otherNode == 8 || otherNode == 9) {
                    // check if x is closer than previous best for bottomRight
                    if (other.getX() > intersection.getX() && (bottomRight == null || other.getX() < bottomRight.getX())) {
                        // then use it!
                        bottomRight = other;
                    }
                }
            }

            //----------- check for top left node ----------------

            // check if x is within plus minus 5 pixels

            if (other.getX() > intersection.getX() - 5 && other.getX() < intersection.getX() + 5) {
                // check if intersection is of type 1, 2, 4, 5 (top left)
                byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
                if (otherNode == 1 || otherNode == 2 || otherNode == 4 || otherNode == 5) {
                    // check if y is closer than previous best for topLeft
                    if (other.getY() < intersection.getY() && (topLeft == null || other.getY() > topLeft.getY())) {
                        // then use it!
                        topLeft = other;
                    }
                }
            }

            //---------------- check for top right node ----------------

            // it's a candidate if it's of type 2, 3, 5 or 6
            byte otherNode = nodeMatrix[(int) other.getX()][(int) other.getY()];
            if (otherNode == 2 || otherNode == 3 || otherNode == 5 || otherNode == 6) {
                // check if x is more than 5 pixels greater than intersection
                if (other.getX() > intersection.getX() + 15) {
                    // check if y is more than 5 pixels less than intersection
                    if (other.getY() < intersection.getY() - 15) {
                        // check if x and y are closer or equal (+-5 pixels)to the previous best for topRight
                        if (topRight == null) {
                            topRight = other;
                        } else if (other.getX() <= topRight.getX() + 5 && other.getY() >= topRight.getY() - 5) {
                            topRight = other;
                        }
                    }
                }
            }

        }
        if (topRight == null || topLeft == null || bottomRight == null) {
            // log.debug("no cell found for intersection: {}", intersection);
            return Optional.empty();
        }


        // Add the cell to the list and color it in the debug image
        double x = intersection.getX();
        double y = Math.max(topLeft.getY(), topRight.getY());
        //double w = Math.min(topRight.getX(), bottomRight.getX()) - x; // TODO fix top right recognition
        double w = bottomRight.getX() - x;
        double h = Math.min(bottomRight.getY(), intersection.getY()) - y;
        return Optional.of(new Rectangle2D.Double(x, y, w, h));
    }


    /**
     * @param line1 the first line
     * @param line2 the second line
     * @return the intersection point of the two lines
     * @see <a href=https://stackoverflow.com/a/61574355>Stackoverflow</a>
     */
    public static Point2D calculateInterceptionPoint(Line2D line1, Line2D line2) {

        Point2D s1 = line1.getP1();
        Point2D s2 = line1.getP2();
        Point2D d1 = line2.getP1();
        Point2D d2 = line2.getP2();


        double a1 = s2.getY() - s1.getY();
        double b1 = s1.getX() - s2.getX();
        double c1 = a1 * s1.getX() + b1 * s1.getY();

        double a2 = d2.getY() - d1.getY();
        double b2 = d1.getX() - d2.getX();
        double c2 = a2 * d1.getX() + b2 * d1.getY();

        double delta = a1 * b2 - a2 * b1;
        return new Point2D.Double((float) ((b2 * c1 - b1 * c2) / delta), (float) ((a1 * c2 - a2 * c1) / delta));

    }


}
