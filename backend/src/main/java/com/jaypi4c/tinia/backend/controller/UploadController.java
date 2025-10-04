package com.jaypi4c.tinia.backend.controller;

import com.jaypi4c.tinia.backend.api.BmpApiDelegate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController implements BmpApiDelegate {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<Void> bmpUploadPost(MultipartFile file,
                                              Optional<Boolean> processOcr) {
        log.info("Received file: {}, processOcr: {}", file.getOriginalFilename(), processOcr.orElse(false));
        // TODO: implement me
        return ResponseEntity.accepted().build();
    }
}
