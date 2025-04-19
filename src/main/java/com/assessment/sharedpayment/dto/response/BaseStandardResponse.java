package com.assessment.sharedpayment.dto.response;

import com.assessment.sharedpayment.util.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.assessment.sharedpayment.util.ResponseCode.SUCCESS;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class BaseStandardResponse<T> extends BaseResponse{

	private T data;

	public BaseStandardResponse(T data) {
		super(SUCCESS.getCode(), SUCCESS.getDescription());
		this.data = data;
	}

	public BaseStandardResponse(ResponseCode responseCode, T data) {
		super(responseCode.getCode(), responseCode.getDescription());
		this.data = data;
	}
	
	public BaseStandardResponse(ResponseCode responseCode) {
		super(responseCode.getCode(), responseCode.getDescription());
	}
}
