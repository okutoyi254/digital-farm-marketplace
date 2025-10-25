package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.AddProductToCart;
import com.farmplace.digitalmarket.DTO.AddProductToCartResponse;
import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.CustomerRegister;
import com.farmplace.digitalmarket.Model.Product;
import org.springframework.data.domain.Page;

public interface CustomerService {

    public AddProductToCartResponse addProductToCart(AddProductToCart productToCart);
    public CreateAccountDto createAccount(CustomerRegister customerRegister);

}
