package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.*;
import com.farmplace.digitalmarket.Model.ProductsCategory;
import com.farmplace.digitalmarket.repository.ProductCategoryRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private  final ProductCategoryRepository categoryRepository;

    public CustomerController(CustomerService customerService, ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/create/account")
    public ResponseEntity<ApiResponse<CreateAccountDto>>createAccount(CustomerRegister customerRegister){
        CreateAccountDto createAccountDto=customerService.createAccount(customerRegister);

        ApiResponse<CreateAccountDto>response=new ApiResponse<>(LocalDateTime.now(),
                "Account created successfully",true,createAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/addproduct")
    public ResponseEntity<ApiResponse<AddProductToCartResponse>>addProductToCart(AddProductToCart productToCart){

       AddProductToCartResponse addProduct=customerService.addProductToCart(productToCart);

       ApiResponse<AddProductToCartResponse>response=new ApiResponse<>(LocalDateTime.now(),"Added successfully",true,addProduct);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<?> findProductByCategoryName(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productName") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        ProductsCategory category = categoryRepository.findByCategoryName(categoryName);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No category found matching '" + categoryName + "'"));
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("The category id is:"+category.getCategoryId());
        Page<ProductDTO> products = productRepository.findProductsByCategoryId(pageable, category.getCategoryId());

        if (products.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "category", category.getCategoryName(),
                    "totalProducts", 0,
                    "message", "No products found under category '" + categoryName + "'"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "category", category.getCategoryName(),
                "totalProducts", products.getTotalElements(),
                "page", products.getNumber(),
                "data", products.getContent()
        ));
    }

    }




