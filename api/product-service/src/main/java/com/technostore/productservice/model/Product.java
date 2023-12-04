package com.technostore.productservice.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "product")
    private ProductRating productRating;

    @OneToOne(mappedBy = "product")
    private ProductPopularity productPopularity;
}
