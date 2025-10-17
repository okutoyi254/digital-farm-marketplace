package com.farmplace.digitalmarket.Model;

import com.farmplace.digitalmarket.enums.DeliveryS;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "orders")
@ToString(exclude = {"customer", "orderItems"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_status")
    private DeliveryS deliveryStatus;

    @Column(name = "total_items")
    private int totalItems;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem>orderItems;





}
