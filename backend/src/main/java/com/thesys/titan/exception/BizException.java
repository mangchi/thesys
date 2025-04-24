package com.thesys.titan.exception;

import com.thesys.titan.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	public BizException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BizException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
