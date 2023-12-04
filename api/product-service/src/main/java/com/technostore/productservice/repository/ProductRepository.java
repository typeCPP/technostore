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
            "FROM Product p " +
            "left join p.category c " +
            "left join p.productRating pr " +
            "left join p.productPopularity pp " +
            "WHERE p.price BETWEEN :minPrice and :maxPrice " +
            "AND COALESCE((pr.sumRating + 0.0) / (pr.countRating + 0.0), 0) BETWEEN :minRating and :maxRating " +
            "AND (:numberOfCategories=0 or c.id in :categories) " +
            "AND (:word IS NULL or LOWER(p.name) LIKE CONCAT('%',LOWER(:word),'%')) ",
            countQuery = "SELECT count(p) " +
                    "FROM Product p " +
                    "left join p.category c " +
                    "left join p.productRating pr " +
                    "left join p.productPopularity pp " +
                    "WHERE p.price BETWEEN :minPrice and :maxPrice " +
                    "AND COALESCE((pr.sumRating + 0.0) / (pr.countRating + 0.0), 0) BETWEEN :minRating and :maxRating " +
                    "AND (:numberOfCategories=0 or c.id in :categories) " +
                    "AND (:word IS NULL or LOWER(p.name) LIKE CONCAT('%',LOWER(:word),'%'))")
    Page<SearchProductDto> searchProducts(double minRating, double maxRating, double minPrice, double maxPrice,
                                          String word, List<Long> categories, int numberOfCategories,
                                          Pageable pageable);
}
