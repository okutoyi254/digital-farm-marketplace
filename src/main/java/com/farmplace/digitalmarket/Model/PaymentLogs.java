package com.farmplace.digitalmarket.Model;

import com.farmplace.digitalmarket.enums.PaymentM;
import com.farmplace.digitalmarket.enums.PaymentS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payment_records")
public class PaymentLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_status")
    private PaymentS paymentStatus;

    @Column(name = "payment_method")
    private PaymentM paymentMethod;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "amount")
    private double amount;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
