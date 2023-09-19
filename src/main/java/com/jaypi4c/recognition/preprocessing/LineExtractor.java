package com.jaypi4c.recognition.preprocessing;

import com.jaypi4c.recognition.TableExtractor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

        log.info("Extracting lines");
        extractLines();

        BufferedImage newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);

        log.info("removing black areas");
        connectedComponentsLabeling(newImage);

        // TODO update combine lines algorithm to keep intersections
        combineLines();
        // idea: increment line length by up to x pixels and check for intersection with other lines. if found, stop and
        // keep length, if not, undo increment

        newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);

        ImageUtils.saveImage(newImage, "debug/lines.jpg");
    }

    /**
     * Removes the text from the image as it scans for continuous black lines. <br>
     * If n (= 50) pixels are black in a row, it is considered a line. Checking each column and row for lines with this method leaves the images with none of the text.
     * <br>
     * Blackened areas are considered as continuous black lines and therefore remain in the image. Using the connected Components Algorithm on the image can remove those areas.
     */
    private void extractLines() {
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
                        Line2D.Float line = new Line2D.Float(beginX, y, x, y);
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
                        Line2D.Float line = new Line2D.Float(x, beginY, x, y);
                        lines.add(line);
                    }
                    beginY = -1;
                }
            }
        }
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


}
