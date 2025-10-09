package com.farmplace.digitalmarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "location")
    private String location;

    @Column(name = "loyalty_points")
    private double loyaltyPoints;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne( cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "customer")
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.MERGE)
    private ArrayList<Order>orders;

}
