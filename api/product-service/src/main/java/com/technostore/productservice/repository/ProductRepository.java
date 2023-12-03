package com.technostore.productservice.repository;

import java.util.List;

import com.technostore.productservice.dto.SearchProductDto;
import com.technostore.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT new com.technostore.productservice.dto.SearchProductDto(p) " +
            "FROM Product p left join p.category c " +
            "WHERE p.price BETWEEN :minPrice and :maxPrice " +
            "AND (:numberOfCategories=0 or c.id in :categories) " +
            "AND (:word IS NULL or LOWER(p.name) LIKE CONCAT('%',LOWER(:word),'%')) " +
            "GROUP BY p.id",
            countQuery = "SELECT count(p) " +
                    "FROM Product p left join p.category c " +
                    "WHERE p.price BETWEEN :minPrice and :maxPrice " +
                    "AND (:numberOfCategories=0 or c.id in :categories) " +
                    "AND (:word IS NULL or LOWER(p.name) LIKE CONCAT('%',LOWER(:word),'%')) " +
                    "GROUP BY p.id")
    Page<SearchProductDto> searchProducts(double minPrice, double maxPrice, String word, List<Long> categories,
                                 int numberOfCategories, Pageable pageable);
}
