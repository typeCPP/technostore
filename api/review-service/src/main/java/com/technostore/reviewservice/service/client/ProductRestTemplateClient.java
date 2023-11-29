package com.technostore.reviewservice.service.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.technostore.reviewservice.dto.UserDto;

@Component
public class ProductRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public UserDto getUserById(Long id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity;
        responseEntity = restTemplate.exchange(
                "http://user-service/user/id/" + id,
                HttpMethod.GET,
                entity, UserDto.class);
        return (UserDto) responseEntity.getBody();
    }
}
