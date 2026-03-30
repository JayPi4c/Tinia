package de.jaypi4c.tinia.extractor.utils;

import de.jaypi4c.tinia.extractor.recognition.preprocessing.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Slf4j
@Component
public class DebugDrawer {


    @Value("${tinia.debug.drawImages}")
    private boolean debug;
    @Value("${tinia.io.debugFolder}")
    private String debugFolder;

    public void saveDebugImage(BufferedImage image, String name, String filename, int page) {
        if (!debug)
            return;
        ImageUtils.saveImage(image, debugFolder + filename + "/" + page + "/" + name + ".jpg");
    }
}
