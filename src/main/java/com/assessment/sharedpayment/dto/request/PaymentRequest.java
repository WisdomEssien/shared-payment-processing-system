package com.assessment.sharedpayment.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "parentId is required")
    private Long parentId;

    @NotNull(message = "studentId is required")
    private Long studentId;

    @DecimalMax(value = "10000000.0", message = "paymentAmount maximum limit exceeded (10000000.00)")
    @DecimalMin(value = "0.0", inclusive = false, message = "paymentAmount is below allowed limit (0.00)")
    @Digits(integer = 8, fraction = 2, message = "paymentAmount format (xxxxxxxx.xx)")
    private BigDecimal paymentAmount;
}
