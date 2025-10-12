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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        private Customer customer;
        private OrderItem orderItem;
        private Order order;

        @BeforeEach
        void setup(){
            Product product = new Product();
            product.setProductId(1L);
            product.setInitialQuantity(100);
            product.setUnitPrice(100D);

            cart=new Cart();
            cart.setCartId(2L);
            cart.setCartItems(new ArrayList<>());

            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(10);
            cartItem.setDiscountAllowed(100);
            cartItem.setUnitPrice(100D);
            cartItem.setTotalPrice(product.getUnitPrice()*cartItem.getQuantity()- cartItem.getDiscountAllowed());

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


            cart.getCartItems().add(cartItem); // ensure cart has items

            when(customerRepository.findByPhoneNumber("12345")).thenReturn(Optional.of(customer));
            when(cartRepository.findByCustomer_phoneNumber("12345")).thenReturn(Optional.ofNullable(cart));
            when(orderRepository.save(any(Order.class))).thenReturn(order);

            double totalCost = businessService.calculateTotalCost("12345");

            businessService.placeOrder("12345", totalCost);

            assertThat(cart.getCartItems()).isEmpty();

            verify(orderRepository, times(1)).save(any(Order.class));
            verify(orderItemsRepository, atLeastOnce()).saveAll(anyList());
            verify(cartRepository, times(1)).save(cart);
        }
}
