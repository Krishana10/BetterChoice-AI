package com.betterchoice.module.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductAttributeRequest(
        @NotBlank @Size(max = 100) String key,
        @NotBlank String value,
        Integer displayOrder
) {
}
