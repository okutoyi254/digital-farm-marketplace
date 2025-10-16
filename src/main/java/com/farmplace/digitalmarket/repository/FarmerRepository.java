package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer,Integer> {
   Optional<Farmer>findByPhoneNumber(String farmer);
}
