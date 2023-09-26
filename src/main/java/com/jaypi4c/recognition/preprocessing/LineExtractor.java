package com.jaypi4c.recognition.preprocessing;

import com.jaypi4c.utils.DebugDrawer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.rendering.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LineExtractor {

    @Getter
    private List<Line2D> lines;
    private final int BLACK_THRESHOLD = 180;

    private BufferedImage image;

    private final DebugDrawer debugDrawer;

    @Autowired
    public LineExtractor(DebugDrawer debugDrawer) {
        this.debugDrawer = debugDrawer;
    }


    public void execute(BufferedImage img) {
        log.info("Starting line extraction");
        lines = new ArrayList<>();
        image = img;

        log.info("Removing black areas");
        BufferedImage newImage = ImageUtils.deepCopy(image);
        newImage = removeBlackAreas(newImage);
        debugDrawer.saveDebugImage(newImage, "removedBlackAreas");

        log.info("Extracting lines");
        extractLines(newImage);

        newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);
        debugDrawer.saveDebugImage(newImage, "rawLines");

        log.info("Combining lines");
        combineLines();

        log.info("Extending lines");
        extendLines();

        newImage = ImageUtils.createImageWithLines(image.getWidth(), image.getHeight(), lines);
        debugDrawer.saveDebugImage(newImage, "lines");
    }

    /**
     * Removes the text from the image as it scans for continuous black lines. <br>
     * If n (= 50) pixels are black in a row, it is considered a line. Checking each column and row for lines with this method leaves the images with none of the text.
     * <br>
     * Blackened areas are considered as continuous black lines and therefore remain in the image. Using the connected Components Algorithm on the image can remove those areas.
     */
    private void extractLines(BufferedImage image) {
        final int n = 50;
        final int blackThreshold = BLACK_THRESHOLD;

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
    }


    private BufferedImage removeByAverageIntensity(BufferedImage bi) {
        final double threshold = 0.35;

        int[] pixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        int[] result = Arrays.copyOf(pixels, pixels.length);

        final int THREAD_COUNT = 4;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            int spacer = bi.getWidth() / THREAD_COUNT;
            Thread thread = new Thread(() -> {
                for (int x = finalI * spacer; x < (finalI + 1) * spacer; x++) {
                    for (int y = 0; y < bi.getHeight(); y++) {
                        if (getAvg(x, y, bi.getWidth(), bi.getHeight(), pixels) < threshold) {
                            int currentIndex = (y * bi.getWidth() + x);
                            result[currentIndex] = Color.WHITE.getRGB();
                        }
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error("Failed to join thread", e);
            }
        }

        BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        // expand the array to match the image data format
        int[] imageArray = new int[result.length * 3];
        for (int i = 0; i < result.length; i++) {
            int pixel = result[i];
            imageArray[i * 3] = (pixel >> 16) & 0xff;
            imageArray[i * 3 + 1] = (pixel >> 8) & 0xff;
            imageArray[i * 3 + 2] = (pixel) & 0xff;
        }

        image.getRaster().setPixels(0, 0, bi.getWidth(), bi.getHeight(), imageArray);
        return image;
    }

    private double getAvg(int x, int y, int w, int h, int[] pixels) {
        int offset = 5;
        double sum = 0;
        int pixelCounter = 0;
        for (int dx = -offset; dx <= offset; dx++) {
            for (int dy = -offset; dy <= offset; dy++) {
                int neighborX = x + dx;
                int neighborY = y + dy;

                if (neighborX >= 0 && neighborX < w && neighborY >= 0 && neighborY < h) {
                    int neighborIndex = (neighborY * w + neighborX);
                    int pixelVal = pixels[neighborIndex];
                    int neighborRed = (pixelVal >> 16) & 0xff;
                    int neighborGreen = (pixelVal >> 8) & 0xff;
                    int neighborBlue = (pixelVal) & 0xff;
                    pixelCounter++;
                    sum += (neighborRed + neighborGreen + neighborBlue);
                }
            }
        }
        return sum / (3 * 255d) / pixelCounter;
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
            final int lineOffset = 3;
            if (ImageUtils.isHorizontal(line)) {
                line.setLine(line.getX1() - lineOffset, line.getY1(), line.getX2() - lineOffset, line.getY2());
            } else {
                line.setLine(line.getX1(), line.getY1() - lineOffset, line.getX2(), line.getY2() - lineOffset);
            }
            result.add(line);
        }

        log.debug("Ended with {} lines", result.size());
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
        int threshold = BLACK_THRESHOLD;
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
        int threshold = BLACK_THRESHOLD;
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
        while (y2 < image.getHeight() && extension < maxExtension) {
            if (ImageUtils.getGray(image.getRGB(x, y2)) < threshold) {
                line.setLine(line.getX1(), line.getY1(), line.getX2(), y2);
                break;
            }
            y2++;
            extension++;
        }
    }

}
