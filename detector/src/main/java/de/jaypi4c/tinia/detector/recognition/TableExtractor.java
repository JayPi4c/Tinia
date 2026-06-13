package de.jaypi4c.tinia.detector.recognition;

import de.jaypi4c.tinia.detector.autoconfigure.DetectorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableExtractor {

    /*
       for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
        If possible use always this order as it's faster:
        https://stackoverflow.com/a/7750416
     */

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}[.,]\\d{1,2}[.,]\\d{2,4}");
    private static final String TITLE = "Medikationsplan";
    private final LineExtractor lineExtractor;
    private final CellIdentifier cellIdentifier;

    private final DetectorProperties detectorProperties;


    /**
     * Starts the execution of the subtasks
     * - load image from PDF page
     * - extracting the Lines
     * - finding the intersections
     * - labeling the intersections and finding the cells
     */
    public ExtractionResult processPage(UUID fileId, int page, String rawPageText, BufferedImage originalImage) {
        String date = null;
        Rectangle2D[][] table = null;

        // Check if title is present
        if (!rawPageText.contains(TITLE)) {
            log.debug("No title found.");
            if (detectorProperties.isSkipWhenNoHeaderFound()) {
                log.debug("Skipping page because no header was found.");
                return new ExtractionResult(null, null);
            }
        }

        Matcher matcher = DATE_PATTERN.matcher(rawPageText);

        if (matcher.find()) {
            date = matcher.group(0);
            date = date.replace(",", ".");
            log.debug("Found date: {}", date);
        } else {
            log.debug("No date found.");
        }

        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();
        String filename = fileId.toString();

        List<Line2D> lines = lineExtractor.execute(originalImage, filename, page);

        BufferedImage imgInEdit = ImageUtils.createImageWithLines(imageWidth, imageHeight, lines);

        List<Rectangle2D> rawCells = cellIdentifier.execute(imgInEdit, lines, filename, page);

        table = createTable(rawCells);
        return new ExtractionResult(table, date);
    }

    private Rectangle2D[][] createTable(List<Rectangle2D> cells) {
        List<List<Rectangle2D>> rows = new ArrayList<>();
        // iterate through all cells. If there is a cell in a row with roughly the same y value, add it to the row
        // otherwise create a new row
        for (Rectangle2D cell : cells) {
            boolean found = false;
            for (List<Rectangle2D> row : rows) { // check existing rows
                if (Math.abs(row.getFirst().getY() - cell.getY()) < 10) {
                    row.add(cell);
                    found = true;
                    break;
                }
            }
            if (!found) { // create new row
                List<Rectangle2D> newRow = new ArrayList<>();
                newRow.add(cell);
                rows.add(newRow);
            }
        }

        List<Rectangle2D[]> tableRows = new ArrayList<>();

        final int EXPECTED_NUM_CELLS_DOSISSCHEMA_SEPARATED = 11;
        final int EXPECTED_NUM_CELLS_DOSISSCHEMA_COMBINED = 8;
        for (List<Rectangle2D> row : rows) {
            if (row.size() == EXPECTED_NUM_CELLS_DOSISSCHEMA_SEPARATED || row.size() == EXPECTED_NUM_CELLS_DOSISSCHEMA_COMBINED) {
                row.sort((o1, o2) -> (int) (o1.getX() - o2.getX()));
                tableRows.add(row.toArray(new Rectangle2D[0]));
            } else if (Math.abs(row.size() - EXPECTED_NUM_CELLS_DOSISSCHEMA_SEPARATED) > 3) {
                continue; // skip the row as there are too many or too few cells
            } else {
                log.debug("Row does not match expected number of cells. Maybe some cells have not been detected? Found: {}", row.size());
                // TODO find missing cells
            }

        }

        return tableRows.toArray(new Rectangle2D[0][0]);
    }

    public record ExtractionResult(Rectangle2D[][] table, String date) {
        public boolean hasContent() {
            return table != null && table.length > 0;
        }
    }

}
