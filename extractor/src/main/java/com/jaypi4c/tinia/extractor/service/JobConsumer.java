package com.jaypi4c.tinia.extractor.service;

import com.jaypi4c.tinia.extractor.autoconfigure.ExtractorProperties;
import com.jaypi4c.tinia.extractor.config.RabbitConfig;
import com.jaypi4c.tinia.extractor.dto.internal.ExtractorJob;
import com.jaypi4c.tinia.extractor.dto.internal.ExtractorResult;
import com.jaypi4c.tinia.extractor.recognition.CellReader;
import com.jaypi4c.tinia.extractor.recognition.TableExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;

    private final TableExtractor tableExtractor;
    private final CellReader cellReader;

    private final ExtractorProperties extractorProperties;

    @RabbitListener(queues = RabbitConfig.EXTRACTOR_JOBS_QUEUE)
    public void consume(ExtractorJob extractorJob) {
        byte[] documentBytes = extractorJob.document();

        int page = extractorJob.page();
        UUID fileId = extractorJob.fileId();

        String[][] resultTable = null;

        try (PDDocument document = PDDocument.load(documentBytes)) {

            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(page + 1);
            stripper.setEndPage(page + 1);
            String rawPageText = stripper.getText(document);

            PDFRenderer renderer = new PDFRenderer(document);
            // https://stackoverflow.com/a/57724726
            BufferedImage image = renderer.renderImageWithDPI(page, extractorProperties.getPdf().getDpi());


            TableExtractor.ExtractionResult extractionResult = tableExtractor.processPage(fileId, page, rawPageText, image);

            if (extractionResult.hasContent()) {
                Rectangle2D[][] table = extractionResult.table();
                String date = extractionResult.date();

                CellReader.ReadingResult result = cellReader.processPage(page, date, table, document);
                if (result.hasTable()) {
                    resultTable = result.table();
                    print2D(resultTable);
                }
            } else {
                log.debug("No medication table found on page {}", page);
            }
        } catch (IOException e) {
            log.error("Failed to process job {}", extractorJob, e);
        }
        rabbitTemplate.convertAndSend(RabbitConfig.EXTRACTOR_RESULTS_QUEUE, new ExtractorResult(fileId, page, resultTable));
    }

    /**
     * <a href="https://www.geeksforgeeks.org/print-2-d-array-matrix-java/">source</a>
     *
     * @param mat 2D String array
     */
    public void print2D(String[][] mat) {
        // Loop through all rows
        for (String[] strings : mat) System.out.println(Arrays.toString(strings));
    }

}
