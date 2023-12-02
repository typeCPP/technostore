package com.technostore.reviewservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.technostore.reviewservice.dto.ReviewStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technostore.reviewservice.dto.ReviewDto;
import com.technostore.reviewservice.dto.UserDto;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRestTemplateClient userRestTemplateClient;

    @Override
    public ReviewDto getReviewById(Long id, HttpServletRequest request) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty() || reviewOptional.get().getText() == null) {
            throw new EntityNotFoundException("No review with id: " + id);
        }
        Review review = reviewOptional.get();
        UserDto userDto = userRestTemplateClient.getUserById(review.getUserId(), request);
        return ReviewDto.builder()
                .id(review.getId())
                .date(review.getDate())
                .productId(review.getProductId())
                .rate(review.getRate())
                .text(review.getText())
                .userName(userDto.getName() + " " + userDto.getLastname())
                .photoLink(userDto.getImage())
                .build();
    }

    @Override
    public List<ReviewDto> getAllReviewsByProductId(Long productId, HttpServletRequest request) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId).stream()
                .filter(review -> review.getText() != null)
                .toList();
        List<ReviewDto> result = new ArrayList<>();

        for (Review review : reviews) {
            UserDto userDto = userRestTemplateClient.getUserById(review.getUserId(), request);
            result.add(ReviewDto.builder()
                    .id(review.getId())
                    .date(review.getDate())
                    .productId(review.getProductId())
                    .rate(review.getRate())
                    .text(review.getText())
                    .userName(userDto.getName() + " " + userDto.getLastname())
                    .photoLink(userDto.getImage())
                    .build());
        }
        return result;
    }

    @Override
    public Review getReviewByUserIdAndProductId(Long userId, Long productId) {
        Optional<Review> reviewOptional = reviewRepository.findByProductIdAndUserId(productId, userId);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        }
        throw new EntityNotFoundException(
                "Review by user with id" + userId + " for product with id " + productId + " not found");
    }

    @Override
    public Double getProductRatingById(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        if (reviews.isEmpty()) {
            throw new EntityNotFoundException("No product with id: " + productId);
        }
        OptionalDouble avgRating = reviews
                .stream()
                .mapToDouble(Review::getRate)
                .average();
        return avgRating.isPresent() ? avgRating.getAsDouble() : 0.0;
    }

    @Override
    public List<ReviewStatisticDto> getReviewStatisticDto(List<Long> productIds) {
        return productIds.stream()
                .map(id -> {
                    List<Review> reviews = reviewRepository.findAllByProductId(id);
                    return ReviewStatisticDto.builder()
                            .productId(id)
                            .rating(reviews.isEmpty() ? 0.0 :
                                    reviews.stream().map(Review::getRate).mapToDouble(r -> r).sum() / reviews.size())
                            .countReviews(reviews.stream().filter(r -> r.getText() != null).count())
                            .build();
                }).toList();
    }

    @Override
    public void setReview(Long userId, Long productId, int rate, String text) {
        Review review;
        try {
            review = getReviewByUserIdAndProductId(userId, productId);
        } catch (EntityNotFoundException exception) {
            review = new Review();
        }
        review.setDate(Instant.now().toEpochMilli());
        review.setProductId(productId);
        review.setRate(rate);
        review.setText(text);
        review.setUserId(userId);
        reviewRepository.save(review);
    }
}
