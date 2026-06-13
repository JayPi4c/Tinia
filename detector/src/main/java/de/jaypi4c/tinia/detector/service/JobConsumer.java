package de.jaypi4c.tinia.detector.service;


import de.jaypi4c.tinia.common.dto.internal.DetectorJob;
import de.jaypi4c.tinia.common.dto.internal.DetectorResult;
import de.jaypi4c.tinia.common.dto.internal.model.BoundingBox;
import de.jaypi4c.tinia.detector.autoconfigure.DetectorProperties;
import de.jaypi4c.tinia.detector.recognition.TableExtractor;
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
import java.util.UUID;

import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_JOBS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.DETECTOR_RESULTS_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final TableExtractor tableExtractor;
    private final DetectorProperties detectorProperties;

    @RabbitListener(queues = DETECTOR_JOBS_QUEUE)
    public void consume(DetectorJob detectorJob) {
        byte[] documentBytes = detectorJob.document();
        int page = detectorJob.page();
        UUID jobId = detectorJob.jobId();

        Rectangle2D[][] table = null;
        String date = null;

        try (PDDocument document = PDDocument.load(documentBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(page + 1);
            stripper.setEndPage(page + 1);
            String rawPageText = stripper.getText(document);

            PDFRenderer renderer = new PDFRenderer(document);
            // https://stackoverflow.com/a/57724726
            BufferedImage image = renderer.renderImageWithDPI(page, detectorProperties.getPdf().getDpi());

            TableExtractor.ExtractionResult extractionResult = tableExtractor.processPage(jobId, page, rawPageText, image);

            if (extractionResult.hasContent()) {
                table = extractionResult.table();
                date = extractionResult.date();
            } else {
                log.debug("No medication table found on page {}", page);
            }
        } catch (IOException e) {
            log.error("Failed to process job {}", detectorJob, e);
        }
        // TODO find better way to announce a "not-plan-page"
        // if date and table are null, it is currently considered to be no table present.
        rabbitTemplate.convertAndSend(DETECTOR_RESULTS_QUEUE, new DetectorResult(jobId, page, date, table == null ? null : prepare(table), documentBytes));
    }

    private BoundingBox[][] prepare(Rectangle2D[][] table) {
        BoundingBox[][] result = new BoundingBox[table.length][];
        for (int i = 0; i < table.length; i++) {
            result[i] = new BoundingBox[table[i].length];
            for (int j = 0; j < table[i].length; j++)
                result[i][j] = BoundingBox.fromRectangle2D(table[i][j]);
        }
        return result;
    }

}
