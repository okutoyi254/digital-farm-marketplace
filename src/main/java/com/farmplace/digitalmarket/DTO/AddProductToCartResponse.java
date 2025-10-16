package com.farmplace.digitalmarket.DTO;

import lombok.Builder;

@Builder
public record AddProductToCartResponse(String productName,int quantity) {
}
