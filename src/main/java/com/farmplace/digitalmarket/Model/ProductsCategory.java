package com.farmplace.digitalmarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "product_categories")
public class ProductsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String category_name;

    @OneToMany(mappedBy = "productsCategory",orphanRemoval = true,cascade = CascadeType.ALL)
    private ArrayList<Product>products;
}
