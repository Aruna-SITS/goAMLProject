package com.itechro.iaml.exception.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:error_messages.properties")
public class ExceptionMessageFinder {

    @Autowired
    private Environment env;

    public List<AppsErrorMessage> getMessages(List<AppsErrorMessage> appsErrorMessages) {
        List<AppsErrorMessage> result = new ArrayList<>();
        appsErrorMessages.forEach((appsErrorMessage) -> {
            if (StringUtils.isEmpty(appsErrorMessage.getErrorMessage())) {
                AppsErrorMessage errorMessage = new AppsErrorMessage(appsErrorMessage.getErrorCode(),
                        this.getMessage(appsErrorMessage.getErrorCode()));
                result.add(errorMessage);
            } else {
                result.add(appsErrorMessage);
            }
        });
        return result;
    }

    public String getMessage(String errorCode) {
        String message;
        if (StringUtils.isNotEmpty(errorCode)) {
            message = env.getProperty(errorCode);
            if (message == null) {
                message = this.getDefaultMessage();
            }
        } else {
            message = this.getDefaultMessage();
        }

        return message;
    }

    private String getDefaultMessage() {
        return env.getProperty(AppsCommonErrorCode.APPS_DEFAULT_ERROR);
    }
}
