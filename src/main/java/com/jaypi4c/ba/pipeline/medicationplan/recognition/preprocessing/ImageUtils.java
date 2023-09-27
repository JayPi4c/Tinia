package com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;

import java.awt.*;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageUtils {

    public static void saveImage(BufferedImage image, String pathname) {
        try {
            File savePath = new File(pathname);
            if (!savePath.exists()) {
                if (savePath.mkdirs()) {
                    log.debug("Created directory {}", savePath.getAbsolutePath());
                } else {
                    log.error("Failed to create directory {}", savePath.getAbsolutePath());
                }
            }
            ImageIO.write(image, "JPEG", new File(pathname));
        } catch (IOException e) {
            log.error("Failed to write debug image", e);
        }
    }

    public static BufferedImage createImageWithLines(int width, int height, List<Line2D> lines) {
        BufferedImage image = createWhiteBackgroundImage(width, height);

        for (Line2D line : lines) {
            drawBlackLine(image, line);
        }
        return image;
    }

    public static void drawBlackLine(BufferedImage image, Line2D line) {
        drawLine(image, line, Color.BLACK);
    }

    public static void drawLine(BufferedImage image, Line2D line, Color color) {
        for (int x_ = (int) line.getX1(); x_ <= line.getX2(); x_++) {
            if (x_ < 0 || x_ >= image.getWidth()) {
                continue;
            }
            for (int y_ = (int) line.getY1(); y_ <= line.getY2(); y_++) {
                if (y_ < 0 || y_ >= image.getHeight()) {
                    continue;
                }
                image.setRGB(x_, y_, color.getRGB());
            }
        }
    }

    public static void drawRandomColorLine(BufferedImage image, Line2D line) {
        Color color = randomColor();
        drawLine(image, line, color);
    }

    public static Color randomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }

    public static boolean isBlack(BufferedImage image, int x, int y, int threshold, boolean xAxis) {
        if (xAxis) {
            for (int i = y - 1; i <= y + 1; i++) {
                if (i < 0 || i >= image.getHeight()) {
                    continue;
                }
                if (getGray(image.getRGB(x, i)) < threshold) {
                    return true;
                }
            }
        } else {
            for (int i = x - 1; i <= x + 1; i++) {
                if (i < 0 || i >= image.getWidth()) {
                    continue;
                }
                if (getGray(image.getRGB(i, y)) < threshold) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getGray(int rgb) {
        int red = (rgb & 0x00ff0000) >> 16;
        int green = (rgb & 0x0000ff00) >> 8;
        int blue = rgb & 0x000000ff;
        return (red + green + blue) / 3;
    }

    public static boolean checkAreaAroundForBlackPixel(BufferedImage image, Point2D center, int offset) {
        for (double i = center.getX() - offset; i < center.getX() + offset; i++) {
            if (i < 0 || i >= image.getWidth()) {
                continue;
            }
            for (double j = center.getY() - offset; j < center.getY() + offset; j++) {
                if (j < 0 || j >= image.getHeight()) {
                    continue;
                }
                if (getGray(image.getRGB((int) i, (int) j)) < 50) {
                    return true;
                }
            }
        }
        return false;
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

    /**
     * <a href="https://stackoverflow.com/a/3514297">...</a>
     *
     * @param bi the image to copy
     * @return a deep copy of the image
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static boolean isHorizontal(Line2D line) {
        return Math.abs(line.getX1() - line.getX2()) > Math.abs(line.getY1() - line.getY2());
    }


}
