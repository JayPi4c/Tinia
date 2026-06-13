package de.jaypi4c.tinia.common.dto.internal;

import de.jaypi4c.tinia.common.dto.internal.model.BoundingBox;

import java.io.Serializable;
import java.util.UUID;

public record DetectorResult(
        UUID jobId,
        int page,
        String date,
        BoundingBox[][] table,
        byte[] documentBytes
) implements Serializable {
    public boolean hasContent() {
        return table != null;
    }
}
