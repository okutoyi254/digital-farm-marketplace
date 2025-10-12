package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    public Optional<Cart>findByCustomer_phoneNumber(String phoneNumber);
}
