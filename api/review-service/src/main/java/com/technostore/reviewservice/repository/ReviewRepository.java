package com.technostore.reviewservice.repository;

import java.util.List;
import java.util.Optional;

import com.technostore.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long productId);

    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
}
