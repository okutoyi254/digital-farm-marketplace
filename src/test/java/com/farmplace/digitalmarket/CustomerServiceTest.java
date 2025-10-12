package com.farmplace.digitalmarket;

import com.farmplace.digitalmarket.DTO.AddProductToCart;
import com.farmplace.digitalmarket.Model.Cart;
import com.farmplace.digitalmarket.Model.CartItem;
import com.farmplace.digitalmarket.Model.Customer;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.repository.CartItemRepository;
import com.farmplace.digitalmarket.repository.CartRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    Cart cart;
    Product product;
    Customer customer;
    @BeforeEach
    void init(){

        product =Product.builder().productId(1L).unitPrice(100D).initialQuantity(200).build();
        cart =Cart.builder().cartId(1L).cartItems(new ArrayList<>()).build();
        customer =Customer.builder().customerId(10L).cart(cart).phoneNumber("12345").build();


    }
    @Test
    void test_add_product_to_cart_returns_success(){

        AddProductToCart request=new AddProductToCart(1L,2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByCustomer_phoneNumber(any(String.class))).thenReturn(Optional.of(cart));

        customerService.addProductToCart(request);

        verify(cartItemRepository,times(1)).save(any(CartItem.class));
        verify(cartRepository,times(1)).save(cart);

        assertEquals(1,cart.getCartItems().size());
        CartItem item=cart.getCartItems().getFirst();
        assertEquals(2,item.getQuantity());
        assertEquals(100.0,item.getUnitPrice());

    }

}
