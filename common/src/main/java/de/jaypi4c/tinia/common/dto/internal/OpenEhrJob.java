package de.jaypi4c.tinia.common.dto.internal;

import java.util.UUID;

public record OpenEhrJob(UUID fileId, int page, String[][] tableData, String date, String metadata) {
}
