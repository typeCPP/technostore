package com.technostore.productservice.model;

import javax.persistence.*;

import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    private double price;

    private String linkPhoto;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductRating> productRating;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductPopularity> productPopularity;
}
