package com.technostore.productservice.service.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technostore.productservice.dto.InCartCountProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public List<InCartCountProductDto> getInCartCountByProductIds(List<Long> productsIds, HttpServletRequest request) {
        StringBuilder idsStr = new StringBuilder();
        for (Long id : productsIds) {
            idsStr.append(id);
            idsStr.append(",");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    "http://order-service/order/get-in-cart-count-by-product-ids?ids=" + idsStr,
                    HttpMethod.GET,
                    entity, List.class);
            ObjectMapper mapper = new ObjectMapper();
            List<InCartCountProductDto> list = new ArrayList<>();
            for (int i = 0; i < responseEntity.getBody().size(); i++) {
                list.add(mapper.convertValue(responseEntity.getBody().get(i), InCartCountProductDto.class));
            }
            return list;
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }
}
