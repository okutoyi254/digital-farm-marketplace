package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.FarmerRegister;
import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.Model.Product;

public interface FarmerService {

    public CreateAccountDto register(FarmerRegister farmerRegister);
    public Product postProduct(PostProductRequest postRequest);
}
