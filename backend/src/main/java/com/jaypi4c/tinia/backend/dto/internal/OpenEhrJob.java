package com.jaypi4c.tinia.backend.dto.internal;

import java.util.UUID;

public record OpenEhrJob(UUID fileId, int page, String[][] tableData, String date, String metadata) {
}
