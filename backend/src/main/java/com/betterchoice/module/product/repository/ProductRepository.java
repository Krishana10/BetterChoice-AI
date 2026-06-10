package com.betterchoice.module.product.repository;

import com.betterchoice.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndActiveTrue(String slug);

    Optional<Product> findByIdAndActiveTrue(UUID id);
}
