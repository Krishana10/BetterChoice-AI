package com.betterchoice.module.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 50)
    private String platform;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(length = 500)
    private String url;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;
}
