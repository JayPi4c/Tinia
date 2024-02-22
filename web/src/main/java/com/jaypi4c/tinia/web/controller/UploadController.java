package com.jaypi4c.tinia.web.controller;

import com.jaypi4c.tinia.web.service.IUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private static final String UPLOAD_VIEW = "upload";
    private final IUploadService uploadService;

    @GetMapping
    public String upload() {
        return UPLOAD_VIEW;
    }

    @PostMapping
    public String uploadFile(Model model, @RequestParam("fileInput") MultipartFile file, @RequestParam("consentCheck") boolean consent) {
        if (!consent) {
            model.addAttribute("output", "You must consent to the processing of your data.");
            return UPLOAD_VIEW;
        }
        String output = "Error reading file";
        try (InputStream inputStream = file.getInputStream()) {
            output = uploadService.processFile(inputStream);
            log.info("{} loaded.", file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Error reading file", e);
        }
        model.addAttribute("output", output);
        return UPLOAD_VIEW;
    }

}
