package com.technostore.reviewservice.integration;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static com.technostore.reviewservice.ReviewTestFactory.buildReview;
import static com.technostore.reviewservice.TestUtils.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTest {
    private final static String JWT
            = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM review.review;");
    }

    @DisplayName("Внесение в базу данных нового отзыва о товаре")
    @Test
    void setReviewTest() throws Exception {
        Review review1 = reviewRepository.save(buildReview(3L, 1));
        Review review2 = reviewRepository.save(buildReview(2L, 10));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(post("/review/newReview")
                        .param("text", "Nice review")
                        .param("rate", "7")
                        .param("productId", "1")
                        .headers(headers))
                .andExpect(status().is2xxSuccessful());

        Optional<Review> review = reviewRepository.findByProductIdAndUserId(1L, 1L);
        assertThat(review).isPresent();
        assertThat(review.get().getUserId()).isEqualTo(1L);
        assertThat(review.get().getProductId()).isEqualTo(1L);
        assertThat(review.get().getText()).isEqualTo("Nice review");
        assertThat(review.get().getRate()).isEqualTo(7);

        assertThat(reviewRepository.findAllByProductId(1L).size()).isEqualTo(3);
    }

    @DisplayName("Получение всех отзывов по товару")
    @Test
    void getAllReviewsByProductIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 1));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        List<Review> expectedReviews = List.of(review, review2);

        mockMvc.perform(get("/review/all-by-product-id/" + 1).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("integration/all-reviews-by-product-id.json"),
                                review2.getId(), review.getId()), true))
                .andExpect(jsonPath("$.[?(@.id == '" + review.getId() + "')].userName").value("Анастасия Иванова"))
                .andExpect(jsonPath("$.[?(@.id == '" + review2.getId() + "')].userName").value("Андрей Петров"))
                .andExpect(jsonPath("$.[?(@.id == '" + review.getId() + "')].photoLink").value("/user/image?id=1"))
                .andExpect(jsonPath("$.[?(@.id == '" + review2.getId() + "')].photoLink").value("/user/image?id=2"))
                .andExpect(jsonPath("$.[?(@.id == '" + review.getId() + "')].text").value(review.getText()))
                .andExpect(jsonPath("$.[?(@.id == '" + review2.getId() + "')].text").value(review2.getText()))
                .andExpect(jsonPath("$.[?(@.id == '" + review.getId() + "')].rate").value(review.getRate()))
                .andExpect(jsonPath("$.[?(@.id == '" + review2.getId() + "')].rate").value(review2.getRate()));
    }

    @DisplayName("Получение отзыва текущего пользователя по товару")
    @Test
    void getReviewByProductIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 8));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/review/by-product-id/" + 1).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(String.format(getFileContent("integration/review-by-product-id.json"),
                        review.getId()), true));
    }
}
