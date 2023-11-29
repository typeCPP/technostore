package com.technostore.productservice.service.client;

import com.technostore.productservice.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
}
