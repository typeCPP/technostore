package com.technostore.reviewservice.integration;

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
import static com.technostore.reviewservice.ReviewTestFactory.mockUserService;
import static com.technostore.reviewservice.TestUtils.getFileContent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTest {
    private final static String JWT
            = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA";
    private final static long userId = 1L;

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM review.review;");
    }

    @Test
    void getAllReviewsByProductIdTest() throws Exception {
        Review review = reviewRepository.save(buildReview(1L, 7));
        Review review2 = reviewRepository.save(buildReview(2L, 1));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JWT);

        mockMvc.perform(get("/review/all-by-product-id/" + 1).headers(headers))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(
                        String.format(getFileContent("integration/all-reviews-by-product-id.json"),
                                review.getId(), review2.getId()), true));
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
