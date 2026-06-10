package com.betterchoice.module.category.service;

import com.betterchoice.core.crud.AbstractCrudService;
import com.betterchoice.core.exception.BusinessException;
import com.betterchoice.module.category.dto.request.CreateCategoryRequest;
import com.betterchoice.module.category.dto.response.CategoryResponse;
import com.betterchoice.module.category.entity.Category;
import com.betterchoice.module.category.repository.CategoryRepository;
import com.betterchoice.shared.enums.CategoryType;
import com.betterchoice.shared.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl
        extends AbstractCrudService<Category, CreateCategoryRequest, CategoryResponse, UUID>
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    protected JpaRepository<Category, UUID> repository() {
        return categoryRepository;
    }

    @Override
    protected String entityName() {
        return "Category";
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException(
                        "NOT_FOUND", "Category not found: " + slug, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> listAll(CategoryType type) {
        List<Category> categories = type == null
                ? categoryRepository.findAllByOrderBySortOrderAscNameAsc()
                : categoryRepository.findByTypeOrderBySortOrderAscNameAsc(type);
        return categories.stream().map(this::toResponse).toList();
    }

    @Override
    protected Category toEntity(CreateCategoryRequest request) {
        String slug = resolveUniqueSlug(SlugUtils.toSlug(request.name()), null);

        Category.CategoryBuilder builder = Category.builder()
                .name(request.name())
                .slug(slug)
                .type(request.type())
                .icon(request.icon())
                .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0);

        if (request.parentId() != null) {
            Category parent = findEntityOrThrow(request.parentId());
            builder.parent(parent);
        }

        return builder.build();
    }

    @Override
    protected void updateEntity(Category entity, CreateCategoryRequest request) {
        entity.setName(request.name());
        entity.setType(request.type());
        entity.setIcon(request.icon());

        if (request.sortOrder() != null) {
            entity.setSortOrder(request.sortOrder());
        }

        if (request.parentId() != null) {
            entity.setParent(findEntityOrThrow(request.parentId()));
        } else {
            entity.setParent(null);
        }

        String baseSlug = SlugUtils.toSlug(request.name());
        if (!baseSlug.equals(entity.getSlug())) {
            entity.setSlug(resolveUniqueSlug(baseSlug, entity.getId()));
        }
    }

    @Override
    protected CategoryResponse toResponse(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getType(),
                entity.getIcon(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getSortOrder(),
                entity.getCreatedAt()
        );
    }

    private String resolveUniqueSlug(String baseSlug, UUID currentId) {
        String slug = baseSlug.isBlank() ? "category" : baseSlug;
        String candidate = slug;
        int suffix = 1;

        while (isSlugTaken(candidate, currentId)) {
            candidate = slug + "-" + suffix++;
        }

        return candidate;
    }

    private boolean isSlugTaken(String slug, UUID currentId) {
        return categoryRepository.findBySlug(slug)
                .map(existing -> currentId == null || !existing.getId().equals(currentId))
                .orElse(false);
    }
}
