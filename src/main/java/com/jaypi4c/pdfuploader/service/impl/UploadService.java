package com.jaypi4c.pdfuploader.service.impl;

import com.jaypi4c.pdfuploader.service.IUploadService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadService implements IUploadService {

    @Override
    public String processFile(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            int pages = document.getNumberOfPages();
            StringBuilder text = new StringBuilder();
            for (int page = 0; page < pages; page++) {
                stripper.setStartPage(page + 1);
                stripper.setEndPage(page + 1);
                text.append(stripper.getText(document));
                if (page < pages - 1) text.append("\n");
            }
            return text.toString();
        } catch (IOException e) {
            return "Reading PDF failed";
        }
    }

}
