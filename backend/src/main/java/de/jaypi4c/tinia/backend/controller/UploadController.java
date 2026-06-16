package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.backend.api.UploadApiDelegate;
import de.jaypi4c.tinia.backend.dto.TaskDto;
import de.jaypi4c.tinia.backend.registry.DocumentRegistry;
import de.jaypi4c.tinia.backend.registry.SseEmitterRegistry;
import de.jaypi4c.tinia.backend.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController implements UploadApiDelegate {

    private final UploadService uploadService;
    private final SseEmitterRegistry sseEmitterRegistry;
    private final DocumentRegistry documentRegistry;

    @Override
    public ResponseEntity<TaskDto> uploadPost(MultipartFile file,
                                              Optional<Boolean> processOcr) {
        UUID jobId = sseEmitterRegistry.register();
        boolean ocr = processOcr.orElse(false);
        log.info("Received file: {}, ocr: {}", file.getOriginalFilename(), ocr);
        uploadService.process(jobId, file, ocr);

        TaskDto task = new TaskDto(jobId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(task);
    }

    @Override
    public ResponseEntity<Resource> uploadJobsJobIdPagesPageNumberImageGet(UUID jobId, Integer pageNumber) {
        try {
            BufferedImage image = documentRegistry.renderPage(jobId, pageNumber);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            log.error("Failed to render image", e);
        }
        return UploadApiDelegate.super.uploadJobsJobIdPagesPageNumberImageGet(jobId, pageNumber);
    }
}
