package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.CartItemDTO;
import com.farmplace.digitalmarket.DTO.CartMessageDTO;
import com.farmplace.digitalmarket.DTO.OrderResponse;
import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.controllers.NotificationSocketController;
import com.farmplace.digitalmarket.exceptions.FailedToFetchCustomerCart;
import com.farmplace.digitalmarket.exceptions.InsufficientBalanceException;
import com.farmplace.digitalmarket.exceptions.NoItemsInTheCartException;
import com.farmplace.digitalmarket.publisher.OrderPlacementEventPublisher;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.serviceInterface.businessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Slf4j
public class BusinessServiceImpl implements businessService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final RabbitTemplate rabbitTemplate;


    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public BusinessServiceImpl(CustomerRepository customerRepository,
                               CartRepository cartRepository, RabbitTemplate rabbitTemplate) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public OrderResponse placeOrder(String username, double paymentAmount) {

        //Fetch customers cart
        Cart cart = cartRepository.findByCustomer_phoneNumber(username).
                orElseThrow(() -> new FailedToFetchCustomerCart("Internal error,try again later"));

        //calculate total cost
        double totalCost = calculateTotalCost(cart);
        if (paymentAmount < totalCost) {
            throw new InsufficientBalanceException("Insufficient balance to place the order,please try again later");
        }

          //validate items
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new NoItemsInTheCartException("No items available,add products to place an order");
        }

        //verify customer
        Customer customer = customerRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new RuntimeException("Internal error,please contact admin"));

        cart.setCustomer(customer);

        List<CartItemDTO> itemDTOS=cart.getCartItems().stream()
                        .map(ci->new CartItemDTO(
                                ci.getProduct().getProductId(),
                                ci.getQuantity(),
                                ci.getTotalPrice(),
                                ci.getDeliveryCharges(),
                                ci.getDiscountAllowed(),
                                ci.getUnitPrice()
                        )).toList();

        CartMessageDTO dto=CartMessageDTO.builder()
                .cartId(cart.getCartId())
                .items(itemDTOS).build();

        log.info("Sending cart to queue for async processing...");
        rabbitTemplate.convertAndSend(exchange,routingKey,dto);


        return OrderResponse.builder().amount(calculateTotalCost(cart)).numberOfItems(cart.getCartItems().size()).build();
    }

    @Override
    public Double calculateTotalCost(Cart cart) {

        return cart.getCartItems().stream().mapToDouble(
                ci->ci.getTotalPrice()+ci.getDeliveryCharges()).sum();

    }

    private double deliveryCharges() {
        return 0.0000;
    }



}
