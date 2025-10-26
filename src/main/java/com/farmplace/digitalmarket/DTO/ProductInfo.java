package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private String productName;
    private Double unitPrice;
    private String imageUrl;
    private Integer currentStock;
    private Boolean available;
    private String shelfLife;
}