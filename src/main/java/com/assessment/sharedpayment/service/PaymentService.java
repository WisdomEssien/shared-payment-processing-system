package com.assessment.sharedpayment.service;

import com.assessment.sharedpayment.config.AppProperty;
import com.assessment.sharedpayment.database.entity.ParentEntity;
import com.assessment.sharedpayment.database.entity.PaymentEntity;
import com.assessment.sharedpayment.database.entity.StudentEntity;
import com.assessment.sharedpayment.database.repository.ParentRepository;
import com.assessment.sharedpayment.database.repository.PaymentRepository;
import com.assessment.sharedpayment.database.repository.StudentRepository;
import com.assessment.sharedpayment.dto.request.PaymentRequest;
import com.assessment.sharedpayment.dto.response.BaseResponse;
import com.assessment.sharedpayment.dto.response.BaseStandardResponse;
import com.assessment.sharedpayment.exception.SharedPaymentException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.assessment.sharedpayment.util.ResponseCode.*;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;
    private final AppProperty appProperty;

    @PostConstruct
    public void initializeDB() {
        ParentEntity parentA = ParentEntity.builder().name("Parent A").balance(0.0).build();
        ParentEntity parentB = ParentEntity.builder().name("Parent B").balance(0.0).build();
        List<ParentEntity> sharedParents = parentRepository.saveAll(List.of(parentA, parentB));

        String sharedParentIds = sharedParents.stream().map(ParentEntity::getId)
                .map(String::valueOf).collect(Collectors.joining(","));
        StudentEntity student1 = StudentEntity.builder().name("Student 1 (Shared)").balance(0.0).parentIds(sharedParentIds).build();
        StudentEntity student2 = StudentEntity.builder().name("Student 2 (Unique A)").balance(0.0).parentIds("1").build();
        StudentEntity student3 = StudentEntity.builder().name("Student 3 (Unique B)").balance(0.0).parentIds("2").build();
        studentRepository.saveAll(List.of(student1, student2, student3));
    }

    @Transactional
    public BaseResponse processPayment(PaymentRequest request) {
        Double paymentAmount = request.getPaymentAmount().doubleValue();
        ParentEntity initiatingParent = parentRepository.findById(request.getParentId())
                .orElseThrow(() -> {
                    log.info("Parent with Id {} not found", request.getParentId());
                    return new SharedPaymentException(UNABLE_TO_LOCATE_RECORD);
                });

        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> {
                    log.info("Student with Id {} not found", request.getStudentId());
                    return new SharedPaymentException(UNABLE_TO_LOCATE_RECORD);
                });

        List<Long> parentIds = hasText(student.getParentIds())
                ? Arrays.asList(student.getParentIds().split(",")).stream()
                .map(Long::parseLong).toList()
                : new ArrayList<>();

        if(!parentIds.contains(request.getParentId())) {
            String errorMessage = String.format("Student[%d] is not associated with Parent[%d]",
                    request.getStudentId(), request.getParentId());
            log.info(errorMessage);
            return new BaseStandardResponse(VALIDATION_ERROR, errorMessage);
        }

        // Fee or Discount Logic
        double adjustedAmount = nonNull(appProperty.getDynamicRate())
                ? paymentAmount * (1 + appProperty.getDynamicRate())
                : paymentAmount;
        log.info("dynamicRate = {}", appProperty.getDynamicRate());
        log.info("adjustedAmount = {}", adjustedAmount);


        log.debug("sharedParentIds :: {}", parentIds);
        boolean isStudentShared = parentIds.size() > 1;

        // Update balances
        if (isStudentShared) {
            log.info("Student with Id {} is shared", request.getStudentId());
            List<ParentEntity> sharedParents = parentRepository.findAllById(parentIds);
            for (ParentEntity parent : sharedParents) {
                parent.setBalance(parent.getBalance() + (adjustedAmount / 2));
            }
            log.debug("Shared parents to be updated :: {}", sharedParents);
            parentRepository.saveAll(sharedParents);
            log.info("Parents balances updated!");
        } else {
            log.info("Student with Id {} is unique", request.getStudentId());
            initiatingParent.setBalance(initiatingParent.getBalance() + adjustedAmount);
            log.debug("Parent to be updated :: {}", initiatingParent);
            parentRepository.save(initiatingParent);
            log.info("Parent balance updated!");
        }

        // Update student balance
        student.setBalance(student.getBalance() + adjustedAmount);
        studentRepository.save(student);
        log.info("Student balance updated!");

        // Record the payment
        PaymentEntity payment = PaymentEntity.builder()
                .parentId(request.getParentId())
                .studentId(request.getStudentId())
                .originalAmount(paymentAmount)
                .adjustedAmount(adjustedAmount)
                .sharedParentIds(parentIds.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .lastModifiedDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        log.info("Transaction logged for audit");

        return new BaseStandardResponse<>(SUCCESS);
    }

}
