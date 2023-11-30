package com.technostore.reviewservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.technostore.reviewservice.dto.ReviewDto;
import com.technostore.reviewservice.model.Review;

public interface ReviewService {
    ReviewDto getReviewById(Long id, HttpServletRequest request);

    List<ReviewDto> getAllReviewsByProductId(Long productId, HttpServletRequest request);

    void setReview(Long userId, Long bookId, int rate, String text);

    Review getReviewByUserIdAndProductId(Long userId, Long productId);

    Double getProductRatingById(Long productId);
}
