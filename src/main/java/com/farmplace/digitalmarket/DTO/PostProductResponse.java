package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PostProductResponse {


    private Long categoryId;
    private String productName;
    private int initialQuantity;
    private double unitPrice;

}
