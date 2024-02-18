package com.technostore.reviewservice;

import com.technostore.reviewservice.dto.ReviewDto;
import com.technostore.reviewservice.dto.ReviewStatisticDto;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import com.technostore.reviewservice.service.ReviewService;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static com.technostore.reviewservice.ReviewTestFactory.buildReview;
import static com.technostore.reviewservice.ReviewTestFactory.mockUserService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewServiceTest {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewService reviewService;
    @MockBean
    UserRestTemplateClient userRestTemplateClient;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM review.review;");
    }

    @Test
    void getReviewByIdTest() {
        Review review = reviewRepository.save(buildReview(1L, 8));
        mockUserService(userRestTemplateClient);

        ReviewDto reviewDto = reviewService.getReviewById(review.getId(), null);
        assertThat(reviewDto.getRate()).isEqualTo(8);
        assertThat(reviewDto.getText()).isEqualTo("Cool review");
        assertThat(reviewDto.getProductId()).isEqualTo(1L);
    }

    @Test
    void getReviewByIdWhenTextIsNullTest() {
        Review review = reviewRepository.save(buildReview(1L, 8));
        review.setText(null);
        reviewRepository.save(review);
        mockUserService(userRestTemplateClient);

        assertThatThrownBy(() -> reviewService.getReviewById(review.getId(), null))
                .isExactlyInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getAllReviewsByProductIdTest() {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        mockUserService(userRestTemplateClient);

        List<ReviewDto> reviewDtoList = reviewService.getAllReviewsByProductId(1L, null);
        assertThat(reviewDtoList.size()).isEqualTo(2);
        assertThat(reviewDtoList.stream().map(ReviewDto::getId))
                .containsExactlyInAnyOrder(review.getId(), review2.getId());
    }

    @Test
    void getAllReviewsByProductIdWhenPartOfReviewHasNullTextTest() {
        Review review = reviewRepository.save(buildReview(1L, 7));
        review.setText(null);
        reviewRepository.save(review);
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        mockUserService(userRestTemplateClient);

        List<ReviewDto> reviewDtoList = reviewService.getAllReviewsByProductId(1L, null);
        assertThat(reviewDtoList.size()).isEqualTo(1);
        assertThat(reviewDtoList.stream().map(ReviewDto::getId))
                .containsExactlyInAnyOrder(review2.getId());
    }

    @Test
    void setReviewTest() {
        reviewService.setReview(1L, 2L, 7, "Nice review");
        Optional<Review> review = reviewRepository.findByProductIdAndUserId(2L, 1L);
        assertThat(review).isPresent();
        assertThat(review.get().getUserId()).isEqualTo(1L);
        assertThat(review.get().getProductId()).isEqualTo(2L);
        assertThat(review.get().getText()).isEqualTo("Nice review");
        assertThat(review.get().getRate()).isEqualTo(7);
    }

    @Test
    void getProductStatisticsTest() {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        Review review3 = reviewRepository.save(buildReview(3L, 10));
        review3.setProductId(105);
        reviewRepository.save(review3);
        mockUserService(userRestTemplateClient);

        List<ReviewStatisticDto> reviewStatisticDtoList = reviewService.getReviewStatisticDto(List.of(1L, 105L));
        assertThat(reviewStatisticDtoList.size()).isEqualTo(2);
        assertThat(reviewStatisticDtoList.get(0).getCountReviews()).isEqualTo(2);
        assertThat(reviewStatisticDtoList.get(0).getRating()).isEqualTo(5);
        assertThat(reviewStatisticDtoList.get(0).getProductId()).isEqualTo(1);
        assertThat(reviewStatisticDtoList.get(1).getCountReviews()).isEqualTo(1);
        assertThat(reviewStatisticDtoList.get(1).getRating()).isEqualTo(10);
        assertThat(reviewStatisticDtoList.get(1).getProductId()).isEqualTo(105);
    }
}
