package com.technostore.reviewservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.technostore.reviewservice.dto.ReviewDto;
import com.technostore.reviewservice.dto.UserDto;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRestTemplateClient userRestTemplateClient;
    @Override
    public ReviewDto getReviewById(Long id, HttpServletRequest request) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("No review with id: " + id);
        }
        Review review = reviewOptional.get();
        UserDto userDto = userRestTemplateClient.getUserById(review.getUserId(), request);
        return ReviewDto.builder()
                .id(review.getId())
                .date(review.getDate().toEpochMilli())
                .productId(review.getProductId())
                .rate(review.getRate())
                .text(review.getText())
                .userName(userDto.getName() + " " + userDto.getLastname())
                .photoLink(userDto.getImage())
                .build();
    }

    @Override
    public List<ReviewDto> getAllReviewsByProductId(Long productId, HttpServletRequest request) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        List<ReviewDto> result = new ArrayList<>();

        for (Review review : reviews) {
            UserDto userDto = userRestTemplateClient.getUserById(review.getUserId(), request);
            result.add(ReviewDto.builder()
                    .id(review.getId())
                    .date(review.getDate().toEpochMilli())
                    .productId(review.getProductId())
                    .rate(review.getRate())
                    .text(review.getText())
                    .userName(userDto.getName() + " " + userDto.getLastname())
                    .photoLink(userDto.getImage())
                    .build());
        }
        return result;
    }
}
