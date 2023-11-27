package com.technostore.reviewservice.service;

import com.technostore.reviewservice.dto.ReviewDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {
    ReviewDto getReviewById(Long id, HttpServletRequest request);

    List<ReviewDto> getAllReviewsByProductId(Long productId, HttpServletRequest request);
}
