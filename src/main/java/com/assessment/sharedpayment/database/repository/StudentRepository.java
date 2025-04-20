package com.assessment.sharedpayment.database.repository;

import com.assessment.sharedpayment.database.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
