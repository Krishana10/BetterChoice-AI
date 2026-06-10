package com.betterchoice.module.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateProductRequest(
        @NotNull UUID categoryId,
        @NotBlank @Size(max = 255) String name,
        String description,
        BigDecimal basePrice,
        @Size(min = 3, max = 3) String currency,
        @Size(max = 500) String imageUrl,
        @Size(max = 150) String brand,
        Map<String, Object> metadata,
        @Valid List<ProductAttributeRequest> attributes
) {
}
