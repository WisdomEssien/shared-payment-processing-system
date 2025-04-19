package com.assessment.sharedpayment.util;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum ResponseCode {

    TIMEOUT("-1", "Timeout"),
    SUCCESS("00", "Successful"),
    PENDING("01", "Pending"),
    UNABLE_TO_LOCATE_RECORD("02", "Cannot Locate Record"),
    GENERIC_ERROR("03", "Something went wrong while trying to process your request"),
    DATABASE_SAVE_ERROR("04", "Error Occurred While Saving To The Database"),
    VALIDATION_ERROR("05", "Validation Error");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ResponseCode getResponseByCode(String code){
        return Arrays.stream(ResponseCode.values()).filter(respCode -> respCode.getCode().equals(code)).findFirst().orElse(null);
    }

}
