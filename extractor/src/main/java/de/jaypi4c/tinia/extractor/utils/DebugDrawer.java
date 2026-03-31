package de.jaypi4c.tinia.extractor.utils;

import de.jaypi4c.tinia.extractor.autoconfigure.DebugProperties;
import de.jaypi4c.tinia.extractor.recognition.preprocessing.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebugDrawer {

    private final DebugProperties debugProperties;

    public void saveDebugImage(BufferedImage image, String name, String filename, int page) {
        if (!debugProperties.isDrawImages())
            return;
        ImageUtils.saveImage(image, debugProperties.getFolder() + filename + "/" + page + "/" + name + ".jpg");
    }
}
