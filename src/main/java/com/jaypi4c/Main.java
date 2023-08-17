package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {

        String pdfPath = "/home/jonas/Studium/cloud/BA/BA Daten/";
        String file1 = "SF_20220104_50335_HA1_LETTER.pdf";
        String file2 = "SF_20220412_50061_HA1_LETTER.pdf";
        String file3= "SF_20220511_50091_HA1_LETTER.pdf";
        String file4 ="SF_20220620_50193_HA1_LETTER.pdf";

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


            ImageIO.write(output, "JPEG", new File(out));
        }

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
