package com.farmplace.digitalmarket.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartMessageDTO {

    private Long cartId;
    private List<CartItemDTO> items;
}
