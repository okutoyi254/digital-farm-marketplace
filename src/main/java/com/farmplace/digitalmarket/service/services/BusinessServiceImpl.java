package com.farmplace.digitalmarket.service.services;

import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.enums.DeliveryS;
import com.farmplace.digitalmarket.exceptions.FailedToFetchCustomerCart;
import com.farmplace.digitalmarket.exceptions.InsufficientBalanceException;
import com.farmplace.digitalmarket.exceptions.NoItemsInTheCartException;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.serviceInterface.businessService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements businessService {

    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    public BusinessServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, PaymentLogsRepository paymentLogs, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentLogs = paymentLogs;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private PaymentLogsRepository paymentLogs;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;



    @Override
    public Order placeOrder(String username, double paymentAmount) {

        if(paymentAmount < calculateTotalCost(username)){
            throw new InsufficientBalanceException("Insufficient balance to place the order,please try again later");
        }

        Cart cart=cartRepository.findByCustomer_phoneNumber(username).
                orElseThrow(()-> new FailedToFetchCustomerCart("Internal error,try again later"));

        if(cart.getCartItems()==null || cart.getCartItems().isEmpty()){
            throw new NoItemsInTheCartException("No items available,add products to place an order");
        }

        Customer customer = customerRepository.findByPhoneNumber(username)
                .orElseThrow(()->new RuntimeException("Internal error,please contact admin"));

        Order order=new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setDeliveryStatus(DeliveryS.PENDING);
        order.setTotalItems(9);

        persistToOrderItems(cart,order);

        persistToPaymentsLogs(order,username,paymentAmount);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return  orderRepository.save(order);
    }

    @Override
    public Double calculateTotalCost(String username) {
        return 0.0;
    }

    private void persistToOrderItems(Cart cart, Order order){

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
        }
        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);
    }

    private void persistToPaymentsLogs(Order order,String username,double paymentAmount){
        double totalPrice = calculateTotalCost(username);
        PaymentLogs payments = new PaymentLogs();
        payments.setOrder(order);
        payments.setPaidAt(LocalDateTime.now());
        payments.setAmount(paymentAmount);
        payments.setTransactionReference(UUID.randomUUID().toString());
        order.setPaymentLogs(payments);
        paymentLogs.save(payments);
    }
}
