package com.jaypi4c.recognition.preprocessing;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LineExtractor {

    @Getter
    private List<Line2D> lines;
    private final BufferedImage image;

    public LineExtractor(BufferedImage image) {
        this.image = image;
        lines = new ArrayList<>();
    }


    public void execute() {

        log.info("removing black areas");
        BufferedImage newImage = ImageUtils.deepCopy(image);
        newImage = removeBlackAreas(newImage);
        ImageUtils.saveImage(newImage, "debug/removedBlackAreas.jpg");

        log.info("Extracting lines");
        extractLines(newImage);

        newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);
        ImageUtils.saveImage(newImage, "debug/rawLines.jpg");

        // TODO update combine lines algorithm to keep intersections
        combineLines();
        // idea: extend line length by up to x pixels and check for intersection with other lines. if found, stop and
        // keep length, if not, undo extension

        extendLines();

        newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);

        ImageUtils.saveImage(newImage, "debug/lines.jpg");
    }

    /**
     * Removes the text from the image as it scans for continuous black lines. <br>
     * If n (= 50) pixels are black in a row, it is considered a line. Checking each column and row for lines with this method leaves the images with none of the text.
     * <br>
     * Blackened areas are considered as continuous black lines and therefore remain in the image. Using the connected Components Algorithm on the image can remove those areas.
     */
    private void extractLines(BufferedImage image) {
        final int n = 50;
        final int blackThreshold = 180;

        log.info("Extracting horizontal lines");
        for (int y = 0; y < image.getHeight(); y++) {
            int beginX = -1;
            for (int x = 0; x < image.getWidth(); x++) {
                if (ImageUtils.isBlack(image, x, y, blackThreshold, true)) {
                    if (beginX < 0) {
                        beginX = x;
                    }
                } else {
                    if (beginX >= 0 && (x - beginX) > n) {
                        Line2D line = new Line2D.Double(beginX, y, x, y);
                        lines.add(line);
                    }
                    beginX = -1;
                }
            }
        }

        log.info("Extracting vertical lines");
        for (int x = 0; x < image.getWidth(); x++) {
            int beginY = -1;
            for (int y = 0; y < image.getHeight(); y++) {
                if (ImageUtils.isBlack(image, x, y, blackThreshold, false)) {
                    if (beginY < 0) {
                        beginY = y;
                    }
                } else {
                    if (beginY >= 0 && (y - beginY) > n) {
                        Line2D line = new Line2D.Double(x, beginY, x, y);
                        lines.add(line);
                    }
                    beginY = -1;
                }
            }
        }
    }

    private BufferedImage removeBlackAreas(BufferedImage bi) {

        return removeByAverageIntensity(bi);

        // connectedComponentsLabeling(bi);
    }


    private BufferedImage removeByAverageIntensity(BufferedImage bi) {
        BufferedImage newImage = ImageUtils.deepCopy(bi);

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                double avg = getAverageIntensity(bi, x, y);
                if (avg < 0.35) {
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return newImage;
    }

    private double getAverageIntensity(BufferedImage bi, int x, int y) {
        int offset = 5;
        int pixelCounter = 0;
        double sum = 0;
        for (int i = x - offset; i <= x + offset; i++) {
            for (int j = y - offset; j <= y + offset; j++) {
                if (i < 0 || i >= bi.getWidth() || j < 0 || j >= bi.getHeight()) {
                    continue;
                }
                pixelCounter++;
                sum += ImageUtils.getGray(bi.getRGB(i, j));
            }
        }
        return sum / 255d / (double)pixelCounter;
    }

    /**
     * ConnectedComponentsAlgorithm to remove black areas from the image
     * <br>
     * See <a href="https://aishack.in/tutorials/labelling-connected-components-example/">Tutorial</a> for reference.
     *
     * <br>
     * As there are only the colors black and white left, the algorithm is straight forward: In the first pass, all
     * black pixels get a label. In the following pass, connected labels will be reduced to on common root label.
     * This leaves connected areas with the same label and the density of black pixels in one connected area can be used
     * to determine if it is a blackened area.
     */
    private void connectedComponentsLabeling(BufferedImage bi) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int[][] labels = new int[imageWidth][imageHeight];
        int label = 1;
        int backgroundLabel = 0;

        Map<Integer, Integer> mergeList = new HashMap<>();

        int backgroundThreshold = 200;

        log.info("starting first pass");
        // first pass
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (ImageUtils.getGray(bi.getRGB(x, y)) > backgroundThreshold) {
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
        for (Chunk chunk : chunks.values()) {
            long avg = 0;
            for (int x = chunk.minX(); x <= chunk.maxX(); x++) {
                for (int y = chunk.minY(); y <= chunk.maxY(); y++) {
                    avg += ImageUtils.getGray(bi.getRGB(x, y));
                }
            }
            // if average color is dark the chunk is probably a blackened area -> remove
            // also if the chunk is too small, meaning just a single line -> remove
            // TODO: maybe a single line should stay...
            avg /= (long) (chunk.maxX() - chunk.minX() + 1) * (chunk.maxY() - chunk.minY() + 1);
            if (avg < 150 || chunk.maxX() - chunk.minX() < 20 || chunk.maxY() - chunk.minY() < 20) {

                // remove all lines from lines list if they are in the blackened chunk
                for (int i = lines.size() - 1; i >= 0; i--) {
                    Line2D line = lines.get(i);
                    if (chunk.contains(line)) {
                        lines.remove(i);
                    }
                }
            }
        }
    }


    private void combineLines() {
        log.debug("Starting with {} lines", lines.size());
        List<Line2D> list = new ArrayList<>(lines);
        List<Line2D> result = new ArrayList<>();

        while (!list.isEmpty()) {
            boolean match = true;
            Line2D line = list.remove(0);
            while (match) {
                LineCombinationResult lcr = combine(line, list);
                match = lcr.match();
                line = lcr.newLine();
                list = lcr.remainingLines();
            }
            // because the combination shifts the line a bit downwards, a small correction is needed
            // it could also be, that the horizontal lines are shifted upwards. But one correction is enough
            if(!ImageUtils.isHorizontal(line)){
                line.setLine(line.getX1(), line.getY1()-5, line.getX2(), line.getY2()-5);
            }
            result.add(line);
        }

        log.debug("ended with {} lines", result.size());
        lines = result;


    }

    private LineCombinationResult combine(Line2D line, List<Line2D> lines) {

        boolean horizontal = ImageUtils.isHorizontal(line);

        int lineThreshold = 15;


        boolean match = false;
        for (int i = lines.size() - 1; i >= 0; i--) {
            Line2D candidate = lines.get(i);
            boolean candidateHorizontal = ImageUtils.isHorizontal(candidate);
            if (horizontal && candidateHorizontal) {
                if (Math.abs(line.getY1() - candidate.getY1()) < lineThreshold) {
                    if (line.getX1() <= candidate.getX2() && line.getX2() >= candidate.getX1()) {
                        match = true;
                        lines.remove(i);
                        line.setLine(Math.min(line.getX1(), candidate.getX1()), line.getY1(), Math.max(line.getX2(), candidate.getX2()), line.getY2());
                    }
                }
            } else if (!horizontal && !candidateHorizontal) {
                if (Math.abs(line.getX1() - candidate.getX1()) < lineThreshold) {
                    if (line.getY1() <= candidate.getY2() && line.getY2() >= candidate.getY1()) {
                        match = true;
                        lines.remove(i);
                        line.setLine(line.getX1(), Math.min(line.getY1(), candidate.getY1()), line.getX2(), Math.max(line.getY2(), candidate.getY2()));
                    }
                }
            }
        }
        return new LineCombinationResult(match, line, lines);
    }

    private record LineCombinationResult(boolean match, Line2D newLine, List<Line2D> remainingLines) {

    }

    private record Chunk(int minX, int minY, int maxX, int maxY) {
        public boolean contains(Line2D line) {
            return line.intersectsLine(minX, minY, maxX, maxY);
        }
    }


    private void extendLines() {
        for (Line2D line : lines) {
            if (ImageUtils.isHorizontal(line)) {
                if (line.getX1() > line.getX2())
                    line.setLine(line.getX2(), line.getY1(), line.getX1(), line.getY2());
                extendHorizontalLine(line);
            } else {
                if (line.getY1() > line.getY2())
                    line.setLine(line.getX1(), line.getY2(), line.getX2(), line.getY1());
                extendVerticalLine(line);
            }
        }
    }

    private void extendHorizontalLine(Line2D line) {
        BufferedImage image = ImageUtils.createImageWithLines(this.image.getWidth(), this.image.getHeight(), lines);
        int x = (int) line.getX1() - 1;
        int y = (int) line.getY1();
        int x2 = (int) line.getX2() + 1;
        int threshold = 180;
        int maxExtension = 15;
        int extension = 0;
        while (x > 0 && extension < maxExtension) {
            if (ImageUtils.getGray(image.getRGB(x, y)) < threshold) {
                line.setLine(x, line.getY1(), line.getX2(), line.getY2());
                break;
            }
            x--;
            extension++;
        }
        extension = 0;
        while (x2 < image.getWidth() && extension < maxExtension) {
            if (ImageUtils.getGray(image.getRGB(x2, y)) < threshold) {
                line.setLine(line.getX1(), line.getY1(), x2, line.getY2());
                break;
            }
            x2++;
            extension++;
        }
    }

    private void extendVerticalLine(Line2D line) {
        BufferedImage image = ImageUtils.createImageWithLines(this.image.getWidth(), this.image.getHeight(), lines);
        int x = (int) line.getX1();
        int y = (int) line.getY1() - 1;
        int y2 = (int) line.getY2() + 1;
        int threshold = 180;
        int maxExtension = 15;
        int extension = 0;
        while (y > 0 && extension < maxExtension) {
            if (ImageUtils.getGray(image.getRGB(x, y)) < threshold) {
                line.setLine(line.getX1(), y, line.getX2(), line.getY2());
                break;
            }
            y--;
            extension++;
        }
        extension = 0;
        while (y2 < image.getWidth() && extension < maxExtension) {
            if (ImageUtils.getGray(image.getRGB(x, y2)) < threshold) {
                line.setLine(line.getX1(), line.getY1(), line.getX2(), y2);
                break;
            }
            y2++;
            extension++;
        }
    }


}
