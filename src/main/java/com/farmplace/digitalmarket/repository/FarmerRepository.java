package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer,Integer> {
}
