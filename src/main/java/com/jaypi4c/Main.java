package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {

        String pdfPath = "/home/jonas/Studium/cloud/BA/BA Daten/";
        String file1 = "SF_20220104_50335_HA1_LETTER.pdf";
        String file2 = "SF_20220412_50061_HA1_LETTER.pdf";
        String file3 = "SF_20220511_50091_HA1_LETTER.pdf";
        String file4 = "SF_20220620_50193_HA1_LETTER.pdf";

        pdfPath += file4;

        String imageResult = "/home/jonas/Studium/cloud/BA/BA Daten/out.jpg";

        filterLines(pdfPath, imageResult);
    }

    //https://stackoverflow.com/a/57724726
    public static void filterLines(String in, String out) throws Exception {

        // read pdf
        try (PDDocument pd = PDDocument.load(new File(in))) {
            PDFRenderer pr = new PDFRenderer(pd);
            // get page as image
            BufferedImage bi = pr.renderImageWithDPI(0, 300);

            // create empty result image
            BufferedImage output = createWhiteBackgroundImage(bi.getWidth(), bi.getHeight());

            // increase contrast
            // bi = increaseContrast(bi, 1.7);


            // perform line detection on image
            final int n = 50; // taken from literature
            final int blackThreshold = 180;

            // horizontal lines
            for (int y = 0; y < bi.getHeight(); y++) {
                int beginX = -1;
                for (int x = 0; x < bi.getWidth(); x++) {
                    int gray = getGray(bi.getRGB(x, y));

                    if (gray < blackThreshold) {
                        if (beginX < 0) {
                            beginX = x;
                        }
                    } else {
                        if (beginX >= 0 && (x - beginX) > n) {
                            for (int i = beginX; i < x; i++) {
                                output.setRGB(i, y, 0x00000000);
                            }

                        }
                        beginX = -1;
                    }
                }
            }

            // vertical lines
            for (int x = 0; x < bi.getWidth(); x++) {
                int beginY = -1;
                for (int y = 0; y < bi.getHeight(); y++) {
                    int gray = getGray(bi.getRGB(x, y));

                    if (gray < blackThreshold) {
                        if (beginY < 0) {
                            beginY = y;
                        }
                    } else {
                        if (beginY >= 0 && (y - beginY) > n) {
                            for (int i = beginY; i < y; i++) {
                                output.setRGB(x, i, 0x00000000);
                            }

                        }
                        beginY = -1;
                    }
                }
            }

            // connected components labeling to remove black areas
            // https://aishack.in/tutorials/labelling-connected-components-example/
            output = connectedComponentsLabeling(output);

            ImageIO.write(output, "JPEG", new File(out));
        }

    }

    public static BufferedImage connectedComponentsLabeling(BufferedImage input) {
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

        //
        Map<Integer, Chunk> chunks = new HashMap<>();

        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
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
            if(avg < 150 || c.maxX() - c.minX() < 20 || c.maxY() - c.minY() < 20) {
                for (int x = c.minX(); x <= c.maxX(); x++) {
                    for (int y = c.minY(); y <= c.maxY(); y++) {
                        input.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }
        }


        return input;
    }




    static Color randomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }


    public static BufferedImage createWhiteBackgroundImage(int width, int height) {
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
