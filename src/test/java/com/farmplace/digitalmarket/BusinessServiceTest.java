package com.farmplace.digitalmarket;

import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.services.BusinessServiceImpl;
import com.farmplace.digitalmarket.service.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {

        @Mock
        private CartRepository cartRepository;

        @Mock private ProductRepository productsRepository;

        @Mock private CartItemRepository cartItemRepository;

        @Mock private PaymentLogsRepository paymentsRepository;

        @Mock private OrderRepository orderRepository;

        @Mock private CustomerRepository customerRepository;

        @Mock private OrderItemRepository orderItemsRepository;

        @InjectMocks
        private BusinessServiceImpl businessService;

        @InjectMocks
        private CustomerServiceImpl customerService;


    private Cart cart;
        private CartItem cartItem;
        private CartItem cartItem1;
        private Customer customer;
        private OrderItem orderItem;
        private Order order;

        @BeforeEach
        void setup(){
            Product product = new Product();
            product.setProductId(1L);
            product.setInitialQuantity(100);
            product.setUnitPrice(100D);

            Product product1=new Product();
            product1.setProductId(20L);
            product1.setInitialQuantity(300);
            product1.setUnitPrice(500D);

            cart=new Cart();
            cart.setCartId(2L);
            cart.setCartItems(new ArrayList<>());

            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(10);
            cartItem.setDiscountAllowed(100);
            cartItem.setUnitPrice(100D);
            cartItem.setTotalPrice(product.getUnitPrice()*cartItem.getQuantity());

            cartItem1=new CartItem();
            cartItem1.setProduct(product1);
            cartItem.setUnitPrice(500D);
            cartItem1.setDiscountAllowed(100);




            customer=new Customer();
            customer.setCustomerId(3L);
            customer.setPhoneNumber("12345");
            customer.setCart(cart);

            order = new Order();
            order.setOrderId(10L);
            order.setCustomer(customer);

            orderItem=new OrderItem();
            orderItem.setOrderItemId(40L);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(10);

        }

        @Test
    void place_an_order_returns_success_200(){


            cart.getCartItems().addAll(List.of(cartItem,cartItem1));

            when(customerRepository.findByPhoneNumber("12345")).thenReturn(Optional.of(customer));
            when(cartRepository.findByCustomer_phoneNumber("12345")).thenReturn(Optional.ofNullable(cart));
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            double totalCost = businessService.calculateTotalCost(cart);

            businessService.placeOrder("12345", totalCost);

            assertThat(cart.getCartItems()).isEmpty();

            verify(orderRepository, times(1)).save(any(Order.class));
            verify(orderItemsRepository, atLeastOnce()).saveAll(anyList());
            verify(cartRepository, times(1)).save(cart);
        }

        @Test
    void test_calculate_totalPrice_returns_success(){
lenient().when(cartRepository.findByCustomer_phoneNumber(any(String.class))).thenReturn(Optional.of(cart));

        double totalCost=businessService.calculateTotalCost(cart);
        System.out.println(totalCost);

        assertThat(totalCost==20000);


        }

}
