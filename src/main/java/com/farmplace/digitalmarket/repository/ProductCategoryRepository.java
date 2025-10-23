package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.ProductsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductsCategory,Long> {

    @Query("""
        SELECT c FROM ProductsCategory c
        WHERE LOWER(TRIM(c.categoryName)) = LOWER(TRIM(:categoryName))
    """)
    ProductsCategory findByCategoryName(String categoryName);
}
