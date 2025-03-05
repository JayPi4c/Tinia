package com.jaypi4c.tinia.core.api;

import com.jaypi4c.tinia.core.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BmpService implements BmpApiDelegate {

    private final Pipeline pipeline;

    @Override
    public ResponseEntity<Void> extractBmp(List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                String name = file.getOriginalFilename();
                pipeline.process(file.getInputStream(), name);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
