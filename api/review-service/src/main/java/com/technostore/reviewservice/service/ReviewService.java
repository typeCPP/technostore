package com.technostore.reviewservice.service;

import com.technostore.reviewservice.dto.ReviewDto;

public interface ReviewService {
    ReviewDto getReviewById(Long id);
}
