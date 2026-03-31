package de.jaypi4c.tinia.backend.service;

import de.jaypi4c.tinia.common.dto.internal.ExtractorJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_JOBS_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final RabbitTemplate rabbitTemplate;

    public void process(MultipartFile file, boolean ocr) {
        UUID fileId = UUID.randomUUID();
        String filename = file.getOriginalFilename();
        try {
            PDDocument document = loadDocument(file.getInputStream());

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                rabbitTemplate.convertAndSend(EXTRACTOR_JOBS_QUEUE, new ExtractorJob(fileId, page, file.getBytes()));
            }
        } catch (IOException e) {
            log.error("Failed to load document", e);
        }
    }

    private PDDocument loadDocument(InputStream inputStream) throws IOException {
        return PDDocument.load(inputStream);
    }
}
