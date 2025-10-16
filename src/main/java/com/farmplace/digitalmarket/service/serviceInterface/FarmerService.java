package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.FarmerRegister;
import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.DTO.PostProductResponse;

public interface FarmerService {

    public CreateAccountDto register(FarmerRegister farmerRegister);
    public PostProductResponse postProduct(PostProductRequest postRequest);
}
