package com.betterchoice.module.product.service;

import com.betterchoice.core.dto.PageResponse;
import com.betterchoice.core.exception.BusinessException;
import com.betterchoice.module.category.entity.Category;
import com.betterchoice.module.category.repository.CategoryRepository;
import com.betterchoice.module.product.dto.request.CreateProductRequest;
import com.betterchoice.module.product.dto.request.ProductAttributeRequest;
import com.betterchoice.module.product.dto.response.ProductAttributeResponse;
import com.betterchoice.module.product.dto.response.ProductResponse;
import com.betterchoice.module.product.dto.response.ProductSourceResponse;
import com.betterchoice.module.product.dto.response.ProductSummaryResponse;
import com.betterchoice.module.product.entity.Product;
import com.betterchoice.module.product.entity.ProductAttribute;
import com.betterchoice.module.product.mapper.ProductMapper;
import com.betterchoice.module.product.repository.ProductAttributeRepository;
import com.betterchoice.module.product.repository.ProductRepository;
import com.betterchoice.module.product.repository.ProductSourceRepository;
import com.betterchoice.module.product.repository.ProductSpecifications;
import com.betterchoice.shared.enums.CategoryType;
import com.betterchoice.shared.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductSourceRepository productSourceRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Category category = findCategoryOrThrow(request.categoryId());
        String slug = resolveUniqueSlug(SlugUtils.toSlug(request.name()), null);

        Product product = Product.builder()
                .category(category)
                .name(request.name())
                .slug(slug)
                .description(request.description())
                .basePrice(request.basePrice())
                .currency(request.currency() != null ? request.currency() : "INR")
                .imageUrl(request.imageUrl())
                .brand(request.brand())
                .metadata(request.metadata() != null ? new HashMap<>(request.metadata()) : new HashMap<>())
                .build();

        applyAttributes(product, request.attributes());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> notFound(id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getBySlug(String slug) {
        Product product = productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new BusinessException(
                        "NOT_FOUND", "Product not found: " + slug, HttpStatus.NOT_FOUND));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(UUID id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> notFound(id));

        Category category = findCategoryOrThrow(request.categoryId());
        product.setCategory(category);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBasePrice(request.basePrice());
        product.setCurrency(request.currency() != null ? request.currency() : product.getCurrency());
        product.setImageUrl(request.imageUrl());
        product.setBrand(request.brand());

        if (request.metadata() != null) {
            product.setMetadata(new HashMap<>(request.metadata()));
        }

        String baseSlug = SlugUtils.toSlug(request.name());
        if (!baseSlug.equals(product.getSlug())) {
            product.setSlug(resolveUniqueSlug(baseSlug, product.getId()));
        }

        product.getAttributes().clear();
        applyAttributes(product, request.attributes());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> notFound(id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> list(
            Pageable pageable,
            UUID categoryId,
            CategoryType categoryType,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String search) {

        Specification<Product> spec = Specification
                .where(ProductSpecifications.isActive())
                .and(ProductSpecifications.hasCategoryId(categoryId))
                .and(ProductSpecifications.hasCategoryType(categoryType))
                .and(ProductSpecifications.minPrice(minPrice))
                .and(ProductSpecifications.maxPrice(maxPrice))
                .and(ProductSpecifications.searchText(search));

        Page<Product> page = productRepository.findAll(spec, pageable);
        return PageResponse.from(page, productMapper::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getAttributes(UUID productId) {
        ensureProductExists(productId);
        return productAttributeRepository.findByProductIdOrderByDisplayOrderAsc(productId).stream()
                .map(productMapper::toAttributeResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSourceResponse> getSources(UUID productId) {
        ensureProductExists(productId);
        return productSourceRepository.findByProductIdOrderByPriceAsc(productId).stream()
                .map(productMapper::toSourceResponse)
                .toList();
    }

    private void applyAttributes(Product product, List<ProductAttributeRequest> attributes) {
        if (attributes == null) {
            return;
        }

        int order = 0;
        for (ProductAttributeRequest attr : attributes) {
            ProductAttribute entity = ProductAttribute.builder()
                    .product(product)
                    .attributeKey(attr.key())
                    .attributeValue(attr.value())
                    .displayOrder(attr.displayOrder() != null ? attr.displayOrder() : order++)
                    .build();
            product.getAttributes().add(entity);
        }
    }

    private Category findCategoryOrThrow(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(
                        "NOT_FOUND", "Category not found: " + categoryId, HttpStatus.NOT_FOUND));
    }

    private void ensureProductExists(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw notFound(productId);
        }
    }

    private BusinessException notFound(UUID id) {
        return new BusinessException("NOT_FOUND", "Product not found: " + id, HttpStatus.NOT_FOUND);
    }

    private String resolveUniqueSlug(String baseSlug, UUID currentId) {
        String slug = baseSlug.isBlank() ? "product" : baseSlug;
        String candidate = slug;
        int suffix = 1;

        while (isSlugTaken(candidate, currentId)) {
            candidate = slug + "-" + suffix++;
        }

        return candidate;
    }

    private boolean isSlugTaken(String slug, UUID currentId) {
        return productRepository.findBySlug(slug)
                .map(existing -> currentId == null || !existing.getId().equals(currentId))
                .orElse(false);
    }
}
