package com.technostore.productservice.model;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @Column(columnDefinition = "bigint default 0")
    private Long sumRating = 0L;

    @Column(columnDefinition = "bigint default 0")
    private Long countRating = 0L;
}
