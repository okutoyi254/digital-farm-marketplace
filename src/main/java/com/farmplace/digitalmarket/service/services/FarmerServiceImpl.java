package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.FarmerRegister;
import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.Model.Farmer;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.repository.FarmerRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.FarmerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FarmerServiceImpl implements FarmerService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final FarmerRepository farmerRepository;

    public FarmerServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, FarmerRepository farmerRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.farmerRepository = farmerRepository;
    }

    @Override
    public CreateAccountDto register(FarmerRegister farmerRegister) {

        Farmer farmer=modelMapper.map(farmerRegister, Farmer.class);
        farmer.setRatings(0.0);
        farmer.setCreatedAt(LocalDateTime.now());
         farmerRepository.save(farmer);

        return CreateAccountDto.builder()
                .firstName(farmerRegister.getFirstName())
                .phoneNumber(farmerRegister.getPhoneNumber()).
                emailAddress(farmerRegister.getEmailAddress()).build();
    }

    @Override
    public Product postProduct(PostProductRequest postRequest) {

        Product product= modelMapper.map(postRequest, Product.class);
        return productRepository.save(product);
    }
}
