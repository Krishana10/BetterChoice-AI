package com.betterchoice.module.product.dto.response;

import com.betterchoice.shared.enums.CategoryType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String slug,
        String description,
        BigDecimal basePrice,
        String currency,
        String imageUrl,
        String brand,
        BigDecimal ratingAvg,
        int reviewCount,
        Map<String, Object> metadata,
        boolean active,
        UUID categoryId,
        String categoryName,
        CategoryType categoryType,
        List<ProductAttributeResponse> attributes,
        List<ProductSourceResponse> sources,
        Instant createdAt,
        Instant updatedAt
) {
}
