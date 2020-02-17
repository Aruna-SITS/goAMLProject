package com.itechro.iaml.model.common;

import java.io.Serializable;

/**
 * @author : chathuranga
 * Date : 7/17/17
 * Time : 5:56 PM
 */

public class Response<T> implements Serializable {

	private boolean success;

	private String message;

	private String errorCode;

	private T result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
