package de.jaypi4c.tinia.extractor.dto.internal;

import java.io.Serializable;
import java.util.UUID;

public record ExtractorJob(UUID fileId, int page, byte[] document) implements Serializable {
}
