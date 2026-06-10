package com.betterchoice.module.product.repository;

import com.betterchoice.module.product.entity.ProductSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductSourceRepository extends JpaRepository<ProductSource, UUID> {

    List<ProductSource> findByProductIdOrderByPriceAsc(UUID productId);
}
