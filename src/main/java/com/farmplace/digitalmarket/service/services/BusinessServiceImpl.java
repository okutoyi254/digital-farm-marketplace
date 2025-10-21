package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.DTO.OrderResponse;
import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.controllers.NotificationSocketController;
import com.farmplace.digitalmarket.enums.DeliveryS;
import com.farmplace.digitalmarket.enums.NotificationStatus;
import com.farmplace.digitalmarket.enums.Roles;
import com.farmplace.digitalmarket.exceptions.FailedToFetchCustomerCart;
import com.farmplace.digitalmarket.exceptions.InsufficientBalanceException;
import com.farmplace.digitalmarket.exceptions.NoItemsInTheCartException;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.serviceInterface.businessService;
import com.farmplace.digitalmarket.utils.LoggedInCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BusinessServiceImpl implements businessService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;
    private final NotificationSocketController socketController;

    public BusinessServiceImpl(CustomerRepository customerRepository,
                               OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, FarmerRepository farmerRepository, NotificationsRepository notificationsRepository, NotificationSocketController socketController,
                               PaymentLogsRepository paymentLogs, CartRepository cartRepository,
                               CartItemRepository cartItemRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
        this.socketController = socketController;
        this.paymentLogs = paymentLogs;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private PaymentLogsRepository paymentLogs;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;


@Transactional
    @Override
    public OrderResponse placeOrder(String username, double paymentAmount) {

         int totalItems = 0;
        Cart cart=cartRepository.findByCustomer_phoneNumber(username).
                orElseThrow(()-> new FailedToFetchCustomerCart("Internal error,try again later"));

        double totalCost=calculateTotalCost(cart);
        if(paymentAmount < totalCost){
            throw new InsufficientBalanceException("Insufficient balance to place the order,please try again later");
        }



        if(cart.getCartItems()==null || cart.getCartItems().isEmpty()){
            throw new NoItemsInTheCartException("No items available,add products to place an order");
        }

        Customer customer = customerRepository.findByPhoneNumber(username)
                .orElseThrow(()->new RuntimeException("Internal error,please contact admin"));

        Order order=new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setDeliveryStatus(DeliveryS.PENDING);
        order.setTotalItems(totalItems);
        orderRepository.save(order);

        persistToOrderItems(cart,order);

        persistToPaymentsLogs(order,cart,paymentAmount);

        cart.getCartItems().clear();
        cartRepository.save(cart);



        return OrderResponse.builder().amount(calculateTotalCost(cart)).numberOfItems(order.getTotalItems()).build();
    }

    @Override
    public Double calculateTotalCost(Cart cart) {

        double totalCost=0.0;
        for(CartItem cartItem :cart.getCartItems()){

            double totalPrice=cartItem.getQuantity()* cartItem.getUnitPrice();
            double deliveries=deliveryCharges();
            double priceOff= cartItem.getDiscountAllowed();
            totalCost+=totalPrice+deliveries-priceOff;

        }
        return totalCost;
    }

    private double deliveryCharges() {
        return 0.0000;
    }

    private void persistToOrderItems(Cart cart, Order order){

        StringBuilder notificationMessage=new StringBuilder();
        List<OrderItem>orderItems=new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems())
        {
            OrderItem orderItem=new OrderItem();
            orderItem.setTotalCost(cartItem.getTotalPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDeliveryCharges(cartItem.getDeliveryCharges());
            orderItem.setDiscount(cartItem.getDiscountAllowed());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            String message=String.valueOf(notificationMessage.append("Customer ").append(LoggedInCustomer.getUsername())
                    .append(" has placed an order for ").append(orderItem.getQuantity())
                    .append(" of ").append(orderItem.getProduct().getProductName()));

            User user= userRepository.findById(Long.valueOf(orderItem.getProduct().getFarmer().getFarmerId())).
                    orElseThrow(()->new RuntimeException("Internal server error,please contact admin"));

            saveNotification(user,message);

          //  totalItems++;

        }
        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);
    }

    private void persistToPaymentsLogs(Order order,Cart cart,double paymentAmount){
        double totalPrice = calculateTotalCost(cart);
        PaymentLogs payments = new PaymentLogs();
        payments.setOrder(order);
        payments.setPaidAt(LocalDateTime.now());
        payments.setAmount(paymentAmount);
        payments.setTransactionReference(UUID.randomUUID().toString());
        paymentLogs.save(payments);
    }

    @Async
    private  void saveNotification(User user,String message) {
        try {
            Notifications notifications = Notifications.builder()
                    .user(user)
                    .notificationMessage(message)
                    .role(Roles.FARMER)
                    .notificationStatus(NotificationStatus.UNREAD)
                    .timeStamp(LocalDateTime.now()).build();
            Notifications saved = notificationsRepository.save(notifications);

            socketController.sendNotificationToFarmer(user.getUserId(), saved);
        } catch (Exception ex){
            log.error("Failed to send notification: {}", ex.getMessage());
        }

    }
}
