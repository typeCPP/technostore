package com.technostore.reviewservice;

import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.repository.ReviewRepository;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class ReviewControllerTest {
    @Autowired
    ReviewRepository reviewRepository;
    @MockBean
    UserRestTemplateClient userRestTemplateClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbcTemplate;

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
        Review review2 = reviewRepository.save(buildReview(2L, 3));
        mockUserService(userRestTemplateClient);

        mockMvc.perform(get("/review/all-by-product-id/" + 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(getFileContent("controller/review/all-reviews-by-product-id.json"), true));
    }
}
