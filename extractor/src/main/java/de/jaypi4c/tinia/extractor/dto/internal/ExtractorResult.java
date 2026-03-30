package de.jaypi4c.tinia.extractor.dto.internal;

import java.util.UUID;

public record ExtractorResult(UUID fileId, int page, String[][] result, String date, String metadata) {
}
