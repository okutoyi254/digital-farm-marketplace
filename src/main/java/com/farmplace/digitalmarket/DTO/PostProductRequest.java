package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostProductRequest {

    private Long categoryId;
    private String productName;
    private int initialQuantity;
    private double unitPrice;
    private String productDescription;
    private String shelfLife;

}
