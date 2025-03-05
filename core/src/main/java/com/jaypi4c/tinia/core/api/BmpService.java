package com.jaypi4c.tinia.core.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class BmpService implements BmpApiDelegate {

    @Override
    public ResponseEntity<Void> extractBmp(List<MultipartFile> files) {
        System.out.println(files.size() + " files received");
        files.stream().map(MultipartFile::getOriginalFilename).forEach(System.out::println);
        return BmpApiDelegate.super.extractBmp(files);
    }
}
