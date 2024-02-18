package com.technostore.productservice.service.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technostore.productservice.dto.ReviewStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.technostore.productservice.dto.ReviewDto;

@Component
public class ReviewRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public List<ReviewDto> getAllReviews(Long productId, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity;
        responseEntity = restTemplate.exchange(
                "http://review-service/review/all-by-product-id/" + productId,
                HttpMethod.GET,
                entity, List.class);
        return (List<ReviewDto>) responseEntity.getBody();
    }

    public double getProductRating(Long productId, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    "http://review-service/review/product-rating/" + productId,
                    HttpMethod.GET,
                    entity, Double.class);
            return ((Double) responseEntity.getBody()).doubleValue();
        } catch (RestClientException e) {
            return 0.0;
        }
    }

    public double getReviewRatingByUserIdAndProductId(Long productId, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    "http://review-service/review/by-product-id/" + productId,
                    HttpMethod.GET,
                    entity, ReviewDto.class);
            return ((ReviewDto) responseEntity.getBody()).getRate();
        } catch (RestClientException e) {
            return 0.0;
        }
    }

    public List<ReviewStatisticDto> getReviewStatisticsByProductIds(List<Long> productsIds) {
        StringBuilder idsStr = new StringBuilder();
        for (Long id : productsIds) {
            idsStr.append(id);
            idsStr.append(",");
        }

        ResponseEntity<List> responseEntity;
        responseEntity = restTemplate.exchange(
                "http://review-service/review/statistics-by-product-ids?ids=" + idsStr,
                HttpMethod.GET,
                null, List.class);
        ObjectMapper mapper = new ObjectMapper();
        List<ReviewStatisticDto> list = new ArrayList<>();
        for (int i = 0; i < responseEntity.getBody().size(); i++) {
            list.add(mapper.convertValue(responseEntity.getBody().get(i), ReviewStatisticDto.class));
        }
        return list;
    }
}
