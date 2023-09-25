package com.jaypi4c.utils;

import com.jaypi4c.recognition.preprocessing.ImageUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

@Slf4j
public class DebugDrawer {

    @Setter
    private static String currentFilename = null;
    @Setter
    private static int currentPage = -1;

    private static final boolean DEBUG = System.getenv("DEBUG") != null && System.getenv("DEBUG").equals("true");

    public static void saveDebugImage(BufferedImage image, String name) {
        if (!DEBUG)
            return;
        ImageUtils.saveImage(image, "/io/debug/" + currentFilename + "/" + currentPage + "/" + name + ".jpg");
    }
}
