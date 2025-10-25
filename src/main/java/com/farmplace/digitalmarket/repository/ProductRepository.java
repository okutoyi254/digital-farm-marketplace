package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.DTO.ProductDTO;
import com.farmplace.digitalmarket.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


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

    /*Create a function in your database and call the function to return the
    JSON format of all the products in the products entity with their characteristics to be displayed
    */
    @Query(value = "SELECT get_all_products_json()", nativeQuery = true)
    String getAllProductsJSON();


}
