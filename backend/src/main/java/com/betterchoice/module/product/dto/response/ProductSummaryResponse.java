package com.betterchoice.module.product.dto.response;

import com.betterchoice.shared.enums.CategoryType;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSummaryResponse(
        UUID id,
        String name,
        String slug,
        String brand,
        BigDecimal basePrice,
        String currency,
        String imageUrl,
        BigDecimal ratingAvg,
        int reviewCount,
        UUID categoryId,
        String categoryName,
        CategoryType categoryType,
        int sourceCount
) {
}
