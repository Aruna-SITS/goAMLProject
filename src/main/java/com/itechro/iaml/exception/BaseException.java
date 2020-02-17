package com.itechro.iaml.exception;

import com.itechro.iaml.commons.constants.AppsConstants;
import com.itechro.iaml.exception.impl.AppsErrorMessage;

import java.util.List;


public interface BaseException {
    List<AppsErrorMessage> getAppsErrorMessages();

    int getHttpStatus();

    AppsConstants.ResponseStatus getResponseStatus();

    Boolean containsErrorCode(String errorCode);

}
