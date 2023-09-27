package com.jaypi4c.ba.pipeline.medicationplan.utils;

import com.jaypi4c.ba.pipeline.medicationplan.recognition.preprocessing.ImageUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Slf4j
@Component
public class DebugDrawer {

    @Setter
    private String currentFilename = null;
    @Setter
    private int currentPage = -1;

    @Value("${debug.drawImages}")
    private boolean debug;
    @Value("${io.debugFolder}")
    private String debugFolder;

    public void saveDebugImage(BufferedImage image, String name) {
        if (!debug)
            return;
        ImageUtils.saveImage(image, debugFolder + currentFilename + "/" + currentPage + "/" + name + ".jpg");
    }
}
