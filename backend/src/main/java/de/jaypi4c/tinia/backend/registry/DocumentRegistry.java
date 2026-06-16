package de.jaypi4c.tinia.backend.registry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentRegistry {

    private final Map<UUID, PDDocument> documentRegistry = new HashMap<>();

    public void register(UUID jobId, PDDocument doc) {
        documentRegistry.put(jobId, doc);
    }

    public PDDocument get(UUID jobId) {
        return documentRegistry.get(jobId);
    }

    public BufferedImage renderPage(UUID jobId, int page) throws IOException {
        PDDocument document = get(jobId);
        if (page >= document.getNumberOfPages() || page < 0)
            throw new IndexOutOfBoundsException("page " + page + " is not in document");
        PDFRenderer renderer = new PDFRenderer(document);
        // https://stackoverflow.com/a/57724726
        return renderer.renderImageWithDPI(page, 300);
    }
}
