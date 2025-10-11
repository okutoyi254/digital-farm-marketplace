package com.farmplace.digitalmarket.repository;

import com.farmplace.digitalmarket.Model.PaymentLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLogsRepository extends JpaRepository<PaymentLogs,Long> {
}
