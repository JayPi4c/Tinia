package com.jaypi4c.tinia.web.service.impl;

import com.jaypi4c.tinia.pipeline.Pipeline;
import com.jaypi4c.tinia.web.service.IUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService implements IUploadService {

    private final Pipeline pipeline;

    @Override
    public String processFile(InputStream inputStream, String filename) {
        List<String> compositions = pipeline.process(inputStream, filename);
        StringBuilder sb = new StringBuilder();
        for (String composition : compositions) {
            sb.append(composition).append("\n");
            sb.append("------------------------\n");
        }
        return sb.toString();
    }

}
