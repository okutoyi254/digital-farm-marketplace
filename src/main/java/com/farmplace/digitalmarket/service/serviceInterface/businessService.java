package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.OrderResponse;
import com.farmplace.digitalmarket.Model.Cart;
import com.farmplace.digitalmarket.Model.Order;

public interface businessService {

    public Order placeOrder(String username, double paymentAmount);
    public  Double calculateTotalCost(Cart cart);
}
