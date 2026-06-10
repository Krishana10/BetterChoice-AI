package com.betterchoice.module.product.dto.response;

import java.util.UUID;

public record ProductAttributeResponse(
        UUID id,
        String key,
        String value,
        int displayOrder
) {
}
