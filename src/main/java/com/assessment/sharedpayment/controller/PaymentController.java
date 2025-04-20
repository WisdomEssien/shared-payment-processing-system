package com.assessment.sharedpayment.controller;

import com.assessment.sharedpayment.dto.request.PaymentRequest;
import com.assessment.sharedpayment.dto.response.BaseResponse;
import com.assessment.sharedpayment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.assessment.sharedpayment.util.AppConstants.PAYMENT;

@Slf4j
@RestController
@RequestMapping(PAYMENT)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public BaseResponse makePayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Request received = {}", request);
        return paymentService.processPayment(request);
    }

}
