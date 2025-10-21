package com.farmplace.digitalmarket;

import com.farmplace.digitalmarket.Model.*;
import com.farmplace.digitalmarket.enums.Roles;
import com.farmplace.digitalmarket.repository.*;
import com.farmplace.digitalmarket.service.services.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebSocketNotificationIntegrationTest {

    private static final String WS_URL = "ws://localhost:8081/ws";
    private static final String FARMER_TOPIC = "/topic/farmer/1"; // ✅ Adjust based on your real farmer ID

    @Autowired
    private BusinessServiceImpl businessService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired private  UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired private NotificationsRepository notificationsRepository;
    @Autowired private PaymentLogsRepository paymentLogsRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderRepository orderRepository;

    @BeforeEach
    void cleanDatabase() {
        // Delete in correct order to respect foreign key constraints
        notificationsRepository.deleteAll();
        paymentLogsRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        farmerRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void whenCustomerPlacesOrder_thenFarmerReceivesNotification() throws Exception {

        String testPhone = "0712345678";

        // Create Farmer

        User farmerUser = new User();
        farmerUser.setRole(Roles.FARMER);


// Create Farmer and link it to User
        Farmer farmer = new Farmer();
        farmer.setFarmerName("John Doe");
        farmer.setUser(farmerUser);
        farmer = farmerRepository.save(farmer);
        farmerUser = userRepository.save(farmerUser);


        // Create Product
        Product product = new Product();
        product.setProductName("Tomatoes");
        product.setFarmer(farmer);
        product.setUnitPrice(100.0);
        product = productRepository.save(product);

        // Create Customer
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");
        customer.setPhoneNumber(testPhone);
        customer = customerRepository.save(customer);

        // Create Cart + Item
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart = cartRepository.save(cart);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(100.0);
        item.setTotalPrice(200.0);
        cartItemRepository.save(item);

        cart.setCartItems(List.of(item));
        cartRepository.save(cart);
        // Async listener for the WebSocket message
        CompletableFuture<String> notificationFuture = new CompletableFuture<>();

        // Setup WebSocket + STOMP client
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Define session handler
        StompSessionHandler handler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("✅ Connected to WebSocket endpoint");

                // Subscribe to farmer’s notification topic
                session.subscribe(FARMER_TOPIC, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Object.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("📩 Farmer notification received: " + payload);
                        notificationFuture.complete(payload.toString());
                    }
                });
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                notificationFuture.completeExceptionally(exception);
            }
        };

        // ✅ Connect asynchronously (non-deprecated)
        CompletableFuture<StompSession> sessionFuture = stompClient.connectAsync(WS_URL, handler);
        StompSession session = sessionFuture.get(5, TimeUnit.SECONDS);

        System.out.println("🚀 Listening for WebSocket notifications...");

        // ✅ Step 1: Simulate placing an order (this triggers async notification)
        String testUsername = "0712345678"; // replace with a valid test customer
        double testPayment = 500.0;

        System.out.println("🛒 Placing order for customer: " + testUsername);
        businessService.placeOrder(testUsername, testPayment);

        // ✅ Step 2: Wait for the WebSocket notification (max 60 seconds)
        String notificationPayload = notificationFuture.get(60, TimeUnit.SECONDS);

        // ✅ Step 3: Disconnect
        session.disconnect();

        // ✅ Step 4: Validate
        assertThat(notificationPayload)
                .as("Farmer should receive an order notification")
                .isNotNull()
                .containsIgnoringCase("order");

        System.out.println("✅ Test passed: Farmer received real-time notification!");
    }
}
