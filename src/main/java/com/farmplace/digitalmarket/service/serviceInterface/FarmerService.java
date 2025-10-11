package com.farmplace.digitalmarket.service.serviceInterface;

import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.Model.Product;

public interface FarmerService {

    public Product postProduct(PostProductRequest postRequest);
}
