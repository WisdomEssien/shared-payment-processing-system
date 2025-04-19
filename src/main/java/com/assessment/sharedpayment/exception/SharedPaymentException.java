package com.assessment.sharedpayment.exception;

import com.assessment.sharedpayment.util.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedPaymentException extends Exception {

    private ResponseCode responseCode;

    public SharedPaymentException(ResponseCode responseCode,
                                  String message, Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public SharedPaymentException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public SharedPaymentException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
