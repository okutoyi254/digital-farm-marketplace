package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.config.ModelMapperConfig;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.FarmerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class FarmerServiceImpl implements FarmerService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public FarmerServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Product postProduct(PostProductRequest postRequest) {

        Product product= modelMapper.map(postRequest, Product.class);
        return productRepository.save(product);
    }
}
