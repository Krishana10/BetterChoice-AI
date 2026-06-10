package com.betterchoice.module.product.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductSourceResponse(
        UUID id,
        String platform,
        String externalId,
        String url,
        BigDecimal price,
        Instant lastSyncedAt
) {
}
