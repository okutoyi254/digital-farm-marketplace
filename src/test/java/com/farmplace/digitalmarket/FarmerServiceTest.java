package com.farmplace.digitalmarket;

import com.farmplace.digitalmarket.DTO.PostProductRequest;
import com.farmplace.digitalmarket.Model.Farmer;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.Model.ProductsCategory;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.services.FarmerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FarmerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FarmerServiceImpl farmerService;

    Farmer mockFarmer;
    ProductsCategory mockProductsCategory;
    Product mockProduct;

    @BeforeEach
    void init()
    {
         mockFarmer= Farmer.builder()
                .farmerId(281)
                .farmerName("James")
                .emailAddress("james@gmail.com").build();

        mockProductsCategory= ProductsCategory.builder()
                .categoryId(20L)
                .category_name("Fruits").build();

        mockProduct=Product.builder()
                .productId(1L)
                .initialQuantity(100)
                .unitPrice(50D)
                .farmer(mockFarmer)
                .productsCategory(mockProductsCategory)
                .productName("Avocado").build();
    }

    @Test
    void add_product_returns_success201_created(){

        PostProductRequest postProductRequest=PostProductRequest.builder().productName("Avocado").
                categoryId(20L).initialQuantity(100).shelfLife("5 days").productDescription("Fresh").build();

        when(modelMapper.map(any(PostProductRequest.class),eq(Product.class))).thenReturn(mockProduct);
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Product saved=farmerService.postProduct(postProductRequest);

        assertEquals("Avocado",saved.getProductName());
        assertEquals(100,saved.getInitialQuantity());
        assertEquals(50D,saved.getUnitPrice());

        verify(productRepository, times(1)).save(any(Product.class));



    }
}
