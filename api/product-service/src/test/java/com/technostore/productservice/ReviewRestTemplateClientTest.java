package com.technostore.productservice;

import com.technostore.productservice.dto.ReviewDto;
import com.technostore.productservice.dto.ReviewStatisticDto;
import com.technostore.productservice.service.client.ReviewRestTemplateClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.technostore.productservice.ProductTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRestTemplateClientTest {
    @Autowired
    ReviewRestTemplateClient reviewRestTemplateClient;
    @MockBean
    RestTemplate restTemplate;

    @Test
    void getAllReviewsTest() {
        ResponseEntity<List> responseEntity =
                new ResponseEntity<>(List.of(buildReviewDto(1L)), HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<List>>any()))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<ReviewDto> reviewDtoList = reviewRestTemplateClient.getAllReviews(1L, request);
        assertThat(reviewDtoList.size()).isEqualTo(1);
        assertThat(reviewDtoList.stream().map(ReviewDto::getProductId).toList().get(0)).isEqualTo(1L);
    }

    @Test
    void getProductRatingTest() {
        ResponseEntity<Double> responseEntity =
                new ResponseEntity<>(5.0, HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Double>>any()))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        Double rating = reviewRestTemplateClient.getProductRating(1L, request);
        assertThat(rating).isEqualTo(5.0);
    }

    @Test
    void getReviewStatisticsByProductIdsTest() {
        ResponseEntity<List> responseEntity =
                new ResponseEntity<>(buildListOfReviewStatisticDto(List.of(1L, 2L)), HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<List>>any()))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<ReviewStatisticDto> reviewStatisticDtoList
                = reviewRestTemplateClient.getReviewStatisticsByProductIds(List.of(1L));
        assertThat(reviewStatisticDtoList.size()).isEqualTo(2);
        assertThat(reviewStatisticDtoList.stream().map(ReviewStatisticDto::getProductId).toList())
                .containsExactlyInAnyOrder(1L, 2L);
    }
}
