package com.technostore.productservice.utils;

import lombok.Builder;

@Builder
public record AppError(
        int statusCode,
        String message
) {
}
