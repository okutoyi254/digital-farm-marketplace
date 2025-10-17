package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.OrderResponse;
import com.farmplace.digitalmarket.Model.Cart;

public interface businessService {

    public OrderResponse placeOrder(String username, double paymentAmount);
    public  Double calculateTotalCost(Cart cart);
}
