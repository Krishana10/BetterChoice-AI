package com.betterchoice.module.category.service;

import com.betterchoice.core.crud.CrudService;
import com.betterchoice.module.category.dto.request.CreateCategoryRequest;
import com.betterchoice.module.category.dto.response.CategoryResponse;
import com.betterchoice.shared.enums.CategoryType;

import java.util.List;
import java.util.UUID;

public interface CategoryService extends CrudService<CreateCategoryRequest, CategoryResponse, UUID> {

    CategoryResponse getBySlug(String slug);

    List<CategoryResponse> listAll(CategoryType type);
}
