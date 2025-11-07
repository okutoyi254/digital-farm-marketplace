package com.farmplace.digitalmarket.Model;

import com.farmplace.digitalmarket.enums.Availability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "shelf_life")
    private String shelfLife;

    @Column(name = "initial_quantity")
    private int initialQuantity;

    @Column(name = "current_stock")
    private int currentStock;

    @Column(name = "discount")
    private double discount;

    @Column(name="allow_discount")
    private boolean discountAllowed;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductsCategory productsCategory;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Column(name = "available")
    @Enumerated(value = EnumType.STRING)
    private Availability availability;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem>cartItems=new ArrayList<>();

}
