package com.itechro.iaml.exception.impl;

import java.io.Serializable;

public class AppsErrorMessage implements Serializable {
	private static final long serialVersionUID = -3720848959116729056L;

	private String errorCode;
	private String errorMessage;

	public AppsErrorMessage(String errorCode) {
		this.errorCode = errorCode;
	}

	public AppsErrorMessage(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public AppsErrorMessage() {
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("AppsErrorMessage{");
		sb.append("errorCode='").append(errorCode).append('\'');
		sb.append(", errorMessage='").append(errorMessage).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
