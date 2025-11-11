package com.farmplace.digitalmarket.consumer;

import com.farmplace.digitalmarket.DTO.CartMessageDTO;
import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.enums.DeliveryS;
import com.farmplace.digitalmarket.publisher.OrderPlacementEventPublisher;
import com.farmplace.digitalmarket.repository.CartRepository;
import com.farmplace.digitalmarket.repository.OrderItemRepository;
import com.farmplace.digitalmarket.repository.OrderRepository;
import com.farmplace.digitalmarket.repository.PaymentLogsRepository;
import com.farmplace.digitalmarket.service.services.BusinessServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class OrderItemsConsumer {


    private final OrderRepository orderRepository;
    private final OrderPlacementEventPublisher orderPlacementEventPublisher;
    private final OrderItemRepository orderItemRepository;
    private final PaymentLogsRepository paymentLogs;
    private final BusinessServiceImpl businessService;
    private final CartRepository cartRepository;

    public OrderItemsConsumer(OrderRepository orderRepository, OrderPlacementEventPublisher orderPlacementEventPublisher, OrderItemRepository orderItemRepository, PaymentLogsRepository paymentLogs, BusinessServiceImpl businessService, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderPlacementEventPublisher = orderPlacementEventPublisher;
        this.orderItemRepository = orderItemRepository;
        this.paymentLogs = paymentLogs;
        this.businessService = businessService;
        this.cartRepository = cartRepository;
    }


    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @RabbitListener(queues = "#{queue.name}")
    public void processCart(CartMessageDTO cartMessage) {

        Cart cart=cartRepository.findById(cartMessage.getCartId()).orElseThrow();

      Order order=createOrder(cart);

      persistPaymentLogs(order);

      processCartItems(cart,order);


    }

    public void persistPaymentLogs(Order order){
        double totalPrice = order.getTotalPrice();
        PaymentLogs payments = new PaymentLogs();
        payments.setOrder(order);
        payments.setPaidAt(LocalDateTime.now());
        payments.setAmount(order.getTotalPrice());
        payments.setTransactionReference(UUID.randomUUID().toString());
        paymentLogs.save(payments);
    }

    public Order createOrder(Cart cart){
        //Order creation
        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryS.PENDING);
        order.setTotalPrice(businessService.calculateTotalCost(cart));
        return orderRepository.save(order);
    }

    public void processCartItems(Cart cart,Order order){

        //Persist order items and publish events
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalCost(cartItem.getTotalPrice());
            orderItem.setDeliveryCharges(cartItem.getDeliveryCharges());
            orderItem.setDiscount(cartItem.getDiscountAllowed());
            orderItems.add(orderItem);

            // Publish event
            Farmer farmer = cartItem.getProduct().getFarmer();
            orderPlacementEventPublisher.publishOrderPlacedEvent(
                    farmer.getUser().getUserId(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getProductName()
            );
        }
        orderItemRepository.saveAll(orderItems);

        cart.getCartItems().clear();

    }
}
