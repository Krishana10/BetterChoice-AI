package com.betterchoice.module.category.dto.response;

import com.betterchoice.shared.enums.CategoryType;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        CategoryType type,
        String icon,
        UUID parentId,
        Integer sortOrder,
        Instant createdAt
) {
}
