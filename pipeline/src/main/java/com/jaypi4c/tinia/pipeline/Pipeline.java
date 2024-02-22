package com.jaypi4c.tinia.pipeline;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class Pipeline {

    public String process(InputStream inputStream) {
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
