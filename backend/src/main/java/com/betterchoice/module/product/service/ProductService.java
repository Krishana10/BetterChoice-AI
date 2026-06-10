package com.betterchoice.module.product.service;

import com.betterchoice.core.dto.PageResponse;
import com.betterchoice.module.product.dto.request.CreateProductRequest;
import com.betterchoice.module.product.dto.response.ProductAttributeResponse;
import com.betterchoice.module.product.dto.response.ProductResponse;
import com.betterchoice.module.product.dto.response.ProductSourceResponse;
import com.betterchoice.module.product.dto.response.ProductSummaryResponse;
import com.betterchoice.shared.enums.CategoryType;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse getById(UUID id);

    ProductResponse getBySlug(String slug);

    ProductResponse update(UUID id, CreateProductRequest request);

    void deactivate(UUID id);

    PageResponse<ProductSummaryResponse> list(
            Pageable pageable,
            UUID categoryId,
            CategoryType categoryType,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String search
    );

    List<ProductAttributeResponse> getAttributes(UUID productId);

    List<ProductSourceResponse> getSources(UUID productId);
}
