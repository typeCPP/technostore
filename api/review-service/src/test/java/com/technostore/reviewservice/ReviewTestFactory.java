package com.technostore.reviewservice;

import com.technostore.reviewservice.dto.UserDto;
import com.technostore.reviewservice.model.Review;
import com.technostore.reviewservice.service.client.UserRestTemplateClient;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ReviewTestFactory {

    public static Review buildReview(long userId, int rate) {
        return Review.builder()
                .text("Cool review")
                .rate(rate)
                .productId(1L)
                .userId(userId)
                .date(1789030338)
                .build();
    }

    public static void mockUserService(UserRestTemplateClient userRestTemplateClient) {
        Mockito.when(userRestTemplateClient.getUserId(any()))
                .thenReturn(1L);
        Mockito.when(userRestTemplateClient.getUserById(eq(1L), any()))
                .thenReturn(new UserDto("Nastia", "Molchanova", "image url"));
        Mockito.when(userRestTemplateClient.getUserById(eq(2L), any()))
                .thenReturn(new UserDto("Andrey", "Usanov", "another image url"));
    }
}
