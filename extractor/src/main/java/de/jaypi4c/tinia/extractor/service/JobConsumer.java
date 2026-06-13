package de.jaypi4c.tinia.extractor.service;

import de.jaypi4c.tinia.common.dto.internal.ExtractorJob;
import de.jaypi4c.tinia.common.dto.internal.ExtractorResult;
import de.jaypi4c.tinia.common.dto.internal.model.BoundingBox;
import de.jaypi4c.tinia.extractor.recognition.CellReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_JOBS_QUEUE;
import static de.jaypi4c.tinia.common.config.RabbitConfig.EXTRACTOR_RESULTS_QUEUE;

/// Service to consume Extractor jobs. This Service should only receive jobs once the Detector module has identified a
/// page as a table and has extracted the individual cells.
@Slf4j
@Service
@RequiredArgsConstructor
public class JobConsumer {

    private final RabbitTemplate rabbitTemplate;

    private final CellReader cellReader;

    @RabbitListener(queues = EXTRACTOR_JOBS_QUEUE)
    public void consume(ExtractorJob extractorJob) {
        byte[] documentBytes = extractorJob.document();
        int page = extractorJob.page();
        UUID jobId = extractorJob.jobId();

        String[][] resultTable = null;
        String date = null;
        String metadata = null;

        try (PDDocument document = PDDocument.load(documentBytes)) {
            Rectangle2D[][] table = prepare(extractorJob.table());
            date = extractorJob.date();

            CellReader.ReadingResult result = cellReader.processPage(page, date, table, document);
            if (result.hasTable()) {
                resultTable = result.table();
                metadata = result.metadata();
                print2D(resultTable);
            }

        } catch (IOException e) {
            log.error("Failed to process job {}", extractorJob, e);
        }
        rabbitTemplate.convertAndSend(EXTRACTOR_RESULTS_QUEUE, new ExtractorResult(jobId, page, resultTable, date, metadata));
    }


    /// Prints a 2D array to the console.
    ///
    /// @param mat 2D String array
    /// @see <a href="https://www.geeksforgeeks.org/print-2-d-array-matrix-java/">source</a>
    public void print2D(String[][] mat) {
        // Loop through all rows
        for (String[] strings : mat) System.out.println(Arrays.toString(strings));
    }

    private Rectangle2D[][] prepare(BoundingBox[][] table) {
        Rectangle2D[][] result = new Rectangle2D[table.length][];
        for (int i = 0; i < table.length; i++) {
            result[i] = new Rectangle2D[table[i].length];
            for (int j = 0; j < table[i].length; j++)
                result[i][j] = table[i][j].toRectangle2D();
        }
        return result;
    }
}
