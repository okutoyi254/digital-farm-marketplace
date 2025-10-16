package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.CreateAccountDto;
import com.farmplace.digitalmarket.DTO.FarmerRegister;
import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.DTO.PostProductResponse;
import com.farmplace.digitalmarket.Model.Farmer;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.Model.ProductsCategory;
import com.farmplace.digitalmarket.repository.FarmerRepository;
import com.farmplace.digitalmarket.repository.ProductCategoryRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.FarmerService;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FarmerServiceImpl implements FarmerService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final FarmerRepository farmerRepository;
    private final ProductCategoryRepository categoryRepository;

    public FarmerServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, FarmerRepository farmerRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.farmerRepository = farmerRepository;
        this.categoryRepository = categoryRepository;
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
    public PostProductResponse postProduct(PostProductRequest postRequest) {

        String farmer= LoggedInCustomer.getUsername();
        Optional<Farmer> farmer1=farmerRepository.findByPhoneNumber("0796791558");
        Optional<ProductsCategory> category=categoryRepository.findById(postRequest.getCategoryId());
        //Product product= modelMapper.map(postRequest, Product.class);
        Product product=Product.builder()
                .productName(postRequest.getProductName())
                .farmer(farmer1.orElse(null))
                .unitPrice(postRequest.getUnitPrice())
                .shelfLife(postRequest.getShelfLife())
                .imageUrl("www")
                .productsCategory(category.orElse(null))
                .currentStock(postRequest.getInitialQuantity())
                .initialQuantity(postRequest.getInitialQuantity())
                .build();
           productRepository.save(product);

        return PostProductResponse.builder()
                .productName(postRequest.getProductName())
                .initialQuantity(postRequest.getInitialQuantity())
                .unitPrice(postRequest.getUnitPrice())
                .categoryId(postRequest.getCategoryId()).build();

    }
}
