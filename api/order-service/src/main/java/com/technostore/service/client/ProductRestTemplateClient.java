package com.technostore.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technostore.dto.FullProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public List<FullProductDto> getProductsByIds(List<Long> productsIds, HttpServletRequest request) {
        StringBuilder idsStr = new StringBuilder();
        for (Long id : productsIds) {
            idsStr.append(id);
            idsStr.append(",");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List> responseEntity;
        responseEntity = restTemplate.exchange(
                "http://product-service/product/products-by-ids/?ids=" + idsStr,
                HttpMethod.GET,
                entity, List.class);

        ObjectMapper mapper = new ObjectMapper();
        List<FullProductDto> list = new ArrayList<>();
        for (int i = 0; i < responseEntity.getBody().size(); i++) {
            list.add(mapper.convertValue(responseEntity.getBody().get(i), FullProductDto.class));
        }
        return list;

    }
}
