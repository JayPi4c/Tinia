package com.jaypi4c.tinia.web.service.impl;

import com.jaypi4c.tinia.pipeline.Pipeline;
import com.jaypi4c.tinia.web.service.IUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class UploadService implements IUploadService {

    private final Pipeline pipeline;

    @Override
    public String processFile(InputStream inputStream, String filename) {
        return pipeline.process(inputStream, filename);
    }

}
