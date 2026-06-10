package com.betterchoice.module.product.controller;

import com.betterchoice.core.dto.ApiResponse;
import com.betterchoice.core.dto.PageResponse;
import com.betterchoice.module.product.dto.request.CreateProductRequest;
import com.betterchoice.module.product.dto.response.ProductAttributeResponse;
import com.betterchoice.module.product.dto.response.ProductResponse;
import com.betterchoice.module.product.dto.response.ProductSourceResponse;
import com.betterchoice.module.product.dto.response.ProductSummaryResponse;
import com.betterchoice.module.product.service.ProductService;
import com.betterchoice.shared.enums.CategoryType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductSummaryResponse>>> list(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) CategoryType categoryType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search) {
        PageResponse<ProductSummaryResponse> page = productService.list(
                pageable, categoryId, categoryType, minPrice, maxPrice, search);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getBySlug(slug)));
    }

    @GetMapping("/{id}/attributes")
    public ResponseEntity<ApiResponse<List<ProductAttributeResponse>>> getAttributes(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getAttributes(id)));
    }

    @GetMapping("/{id}/sources")
    public ResponseEntity<ApiResponse<List<ProductSourceResponse>>> getSources(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getSources(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(productService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        productService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Product deactivated"));
    }
}
