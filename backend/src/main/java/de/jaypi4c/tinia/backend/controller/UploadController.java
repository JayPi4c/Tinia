package de.jaypi4c.tinia.backend.controller;

import de.jaypi4c.tinia.backend.api.BmpApiDelegate;
import de.jaypi4c.tinia.backend.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController implements BmpApiDelegate {

    private final UploadService uploadService;

    @Override
    public ResponseEntity<Void> bmpUploadPost(MultipartFile file,
                                              Optional<Boolean> processOcr) {
        boolean ocr = processOcr.orElse(false);
        log.info("Received file: {}, ocr: {}", file.getOriginalFilename(), ocr);
        uploadService.process(file, ocr);

        return ResponseEntity.accepted().build();
    }
}
