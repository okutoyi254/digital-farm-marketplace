package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.ProductsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductsCategory,Long> {
}
