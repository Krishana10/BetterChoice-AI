package com.betterchoice.module.category.dto.request;

import com.betterchoice.shared.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCategoryRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull CategoryType type,
        @Size(max = 50) String icon,
        UUID parentId,
        Integer sortOrder
) {
}
