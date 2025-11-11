package com.farmplace.digitalmarket.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"cart", "orders"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone_number",unique = true)
    private String phoneNumber;

    @Column(name = "email_address",unique = true)
    private String emailAddress;

    @Column(name = "location")
    private String location;

    @Column(name = "loyalty_points")
    private double loyaltyPoints;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne( cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "customer")
    @JsonBackReference
    private Cart cart;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Order>orders=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userid")
    private User user;


}
