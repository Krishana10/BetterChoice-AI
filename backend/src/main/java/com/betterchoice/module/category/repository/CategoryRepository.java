package com.betterchoice.module.category.repository;

import com.betterchoice.module.category.entity.Category;
import com.betterchoice.shared.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Category> findAllByOrderBySortOrderAscNameAsc();

    List<Category> findByTypeOrderBySortOrderAscNameAsc(CategoryType type);
}
