package com.technostore.reviewservice.service;

import com.technostore.reviewservice.dto.ReviewDto;

import javax.servlet.http.HttpServletRequest;

public interface ReviewService {
    ReviewDto getReviewById(Long id, HttpServletRequest request);
}
