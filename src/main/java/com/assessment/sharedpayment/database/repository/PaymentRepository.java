package com.assessment.sharedpayment.database.repository;

import com.assessment.sharedpayment.database.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
