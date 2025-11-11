package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long productId;
    private int quantity;
    private double totalPrice;
    private double deliveryCharges;
    private double discount;
    private double unitPrice;
}
