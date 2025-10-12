package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.AddProductToCart;
import com.farmplace.digitalmarket.Model.CartItem;

public interface CustomerService {

    public CartItem addProductToCart(AddProductToCart productToCart);
}
