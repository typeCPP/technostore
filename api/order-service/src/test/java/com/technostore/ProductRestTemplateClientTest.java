package com.technostore;

import com.technostore.dto.FullProductDto;
import com.technostore.service.client.ProductRestTemplateClient;
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

import static com.technostore.OrderTestFactory.buildListOfFullProductDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRestTemplateClientTest {
    @Autowired
    ProductRestTemplateClient productRestTemplateClient;
    @MockBean
    RestTemplate restTemplate;

    @Test
    void getProductsByIdsTest() {
        ResponseEntity<List> responseEntity =
                new ResponseEntity<>(buildListOfFullProductDto(1L), HttpStatus.OK);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<List>>any()))
                .thenReturn(responseEntity);
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<FullProductDto> fullProductDtos = productRestTemplateClient.getProductsByIds(List.of(1L), request);
        assertThat(fullProductDtos.size()).isEqualTo(1);
        assertThat(fullProductDtos.get(0).getId()).isEqualTo(1);
    }
}
