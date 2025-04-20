package com.assessment.sharedpayment.database.repository;

import com.assessment.sharedpayment.database.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
}
