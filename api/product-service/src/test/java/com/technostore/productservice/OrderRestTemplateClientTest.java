package com.technostore.productservice;

import com.technostore.productservice.dto.InCartCountProductDto;
import com.technostore.productservice.service.client.OrderRestTemplateClient;
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

import static com.technostore.productservice.ProductTestFactory.buildListOfInCartCountProductDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRestTemplateClientTest {
    @Autowired
    OrderRestTemplateClient orderRestTemplateClient;
    @MockBean
    RestTemplate restTemplate;

    @Test
    void getProductsByIdsTest() {
        ResponseEntity<List> responseEntity =
                new ResponseEntity<>(buildListOfInCartCountProductDto(List.of(1L, 2L)), HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<List>>any()))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<InCartCountProductDto> inCartCountByProductIds
                = orderRestTemplateClient.getInCartCountByProductIds(List.of(1L), request);
        assertThat(inCartCountByProductIds.size()).isEqualTo(2);
        assertThat(inCartCountByProductIds.stream().map(InCartCountProductDto::getProductId).toList())
                .containsExactlyInAnyOrder(1L, 2L);
    }
}
