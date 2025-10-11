package com.jaypi4c.tinia.backend.dto.internal;

import java.util.UUID;

public record ExtractorResult(UUID fileId, int page, String[][] result) {

    public boolean hasResult() {
        return result != null && result.length > 0;
    }


}
