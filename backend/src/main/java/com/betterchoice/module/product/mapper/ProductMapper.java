package com.betterchoice.module.product.mapper;

import com.betterchoice.module.product.dto.response.ProductAttributeResponse;
import com.betterchoice.module.product.dto.response.ProductResponse;
import com.betterchoice.module.product.dto.response.ProductSourceResponse;
import com.betterchoice.module.product.dto.response.ProductSummaryResponse;
import com.betterchoice.module.product.entity.Product;
import com.betterchoice.module.product.entity.ProductAttribute;
import com.betterchoice.module.product.entity.ProductSource;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ProductMapper {

    public ProductSummaryResponse toSummary(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getBrand(),
                product.getBasePrice(),
                product.getCurrency(),
                product.getImageUrl(),
                product.getRatingAvg(),
                product.getReviewCount(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getType(),
                product.getSources() != null ? product.getSources().size() : 0
        );
    }

    public ProductResponse toResponse(Product product) {
        List<ProductAttributeResponse> attributes = product.getAttributes().stream()
                .sorted(Comparator.comparingInt(ProductAttribute::getDisplayOrder))
                .map(this::toAttributeResponse)
                .toList();

        List<ProductSourceResponse> sources = product.getSources().stream()
                .sorted(Comparator.comparing(
                        ProductSource::getPrice,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toSourceResponse)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getBasePrice(),
                product.getCurrency(),
                product.getImageUrl(),
                product.getBrand(),
                product.getRatingAvg(),
                product.getReviewCount(),
                product.getMetadata(),
                Boolean.TRUE.equals(product.getActive()),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getType(),
                attributes,
                sources,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public ProductAttributeResponse toAttributeResponse(ProductAttribute attribute) {
        return new ProductAttributeResponse(
                attribute.getId(),
                attribute.getAttributeKey(),
                attribute.getAttributeValue(),
                attribute.getDisplayOrder()
        );
    }

    public ProductSourceResponse toSourceResponse(ProductSource source) {
        return new ProductSourceResponse(
                source.getId(),
                source.getPlatform(),
                source.getExternalId(),
                source.getUrl(),
                source.getPrice(),
                source.getLastSyncedAt()
        );
    }
}
