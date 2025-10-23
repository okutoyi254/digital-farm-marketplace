package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.DTO.ProductDTO;
import com.farmplace.digitalmarket.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("""
    SELECT new com.farmplace.digitalmarket.DTO.ProductDTO(
        p.productName,
        p.unitPrice,
        p.currentStock,
        p.availability
    )
    FROM Product p
    JOIN p.productsCategory c
    WHERE c.categoryId = :categoryId
    """)
    Page<ProductDTO> findProductsByCategoryId(Pageable pageable,@Param("categoryId") Long categoryId);



}
