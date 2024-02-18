package com.technostore.reviewservice;

import com.technostore.reviewservice.controller.ReviewController;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static com.technostore.reviewservice.ReviewTestFactory.buildReview;
import static com.technostore.reviewservice.ReviewTestFactory.mockUserService;
import static com.technostore.reviewservice.TestUtils.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {
    @Autowired
    ReviewRepository reviewRepository;
    @MockBean
    UserRestTemplateClient userRestTemplateClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ReviewController reviewController;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM review.review;");
    }

    @Test
    void getReviewByIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 8));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/" + review.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/review/review.json"),
                        review.getId()), true));
    }

    @Test
    void getProductRatingTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/product-rating/" + 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("5.0", true));
    }

    @Test
    void getReviewByProductIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 8));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/by-product-id/" + 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("controller/review/review-by-product-id.json"),
                        review.getId()), true));
    }

    @Test
    void getAllReviewsByProductIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 7));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/all-by-product-id/" + 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("controller/review/all-reviews-by-product-id.json"),
                                review.getId()), true));
    }

    @Test
    void tryGetReviewByProductIdWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(get("/review/by-product-id/" + 1))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void tryGetReviewByProductIdWhenReviewNotExistsTest() throws Exception {
        mockUserService(userRestTemplateClient);
        mockMvc.perform(get("/review/by-product-id/" + 1000000))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Review by user with id 1 for product with id 1000000 not found")));
    }

    @Test
    void tryGetRatingByProductIdWhenReviewsForThisProductNotExistsTest() throws Exception {
        mockUserService(userRestTemplateClient);
        mockMvc.perform(get("/review/product-rating/" + 1))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("No product with id: 1")));
    }

    @Test
    void setReviewTest() throws Exception {
        mockUserService(userRestTemplateClient);

        mockMvc.perform(post("/review/newReview")
                        .param("text", "Nice review")
                        .param("rate", "7")
                        .param("productId", "2"))
                .andExpect(status().is2xxSuccessful());

        Optional<Review> review = reviewRepository.findByProductIdAndUserId(2L, 1L);
        assertThat(review).isPresent();
        assertThat(review.get().getUserId()).isEqualTo(1L);
        assertThat(review.get().getProductId()).isEqualTo(2L);
        assertThat(review.get().getText()).isEqualTo("Nice review");
        assertThat(review.get().getRate()).isEqualTo(7);
    }

    @Test
    void trySetReviewWhenLostConnectionWithUserServiceTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(IllegalStateException.class);
        mockMvc.perform(post("/review/newReview")
                        .param("text", "Nice review")
                        .param("rate", "7")
                        .param("productId", "2"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Lost connection with user service")));
    }

    @Test
    void trySetReviewWhenUnauthorizedHttpClientErrorExceptionTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(HttpClientErrorException.Unauthorized.class);
        mockMvc.perform(post("/review/newReview")
                        .param("text", "Nice review")
                        .param("rate", "7")
                        .param("productId", "2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void trySetReviewWhenForbiddenHttpClientErrorExceptionTest() throws Exception {
        when(userRestTemplateClient.getUserId(any())).thenThrow(HttpClientErrorException.Forbidden.class);
        mockMvc.perform(post("/review/newReview")
                        .param("text", "Nice review")
                        .param("rate", "7")
                        .param("productId", "2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getReviewStatisticsByProductIdsTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        Review review3 = reviewRepository.save(buildReview(3L, 10));
        review3.setProductId(105);
        reviewRepository.save(review3);
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/statistics-by-product-ids")
                        .param("ids", "1,105"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(getFileContent("controller/review/product-statistics.json"), true));
    }

    @Test
    void whenThrowHttpClientErrorExceptionThenHandlerHandleItTest() {
        assertThatThrownBy(() -> reviewController.handleException(new HttpClientErrorException(HttpStatus.BAD_REQUEST)))
                .isExactlyInstanceOf(HttpClientErrorException.class);
    }
}
