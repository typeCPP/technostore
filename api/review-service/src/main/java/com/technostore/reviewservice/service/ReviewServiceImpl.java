package com.technostore.reviewservice.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.technostore.reviewservice.dto.ReviewDto;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Override
    public ReviewDto getReviewById(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("No review with id: " + id);
        }
        Review review = reviewOptional.get();
        return ReviewDto.builder()
                .id(review.getId())
                .date(review.getDate().toEpochMilli())
                .productId(review.getProductId())
                .rate(review.getRate())
                .text(review.getText())
                .userName("") // TODO: get from user-service
                .photoLink("") // TODO: get from user-service
                .build();
    }
}
