package com.itechro.iaml.exception.impl;

import com.itechro.iaml.commons.constants.AppsConstants.ResponseStatus;
import com.itechro.iaml.exception.BaseException;
import com.itechro.iaml.exception.aop.ResponseExceptionHandler;
import com.itechro.iaml.model.common.ResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Aspect
public class ExceptionHandlerAOP {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAOP.class);

    @Autowired
    private ExceptionMessageFinder exceptionMessageFinder;

    @Pointcut(
            "within(@org.springframework.web.bind.annotation.RestController *) && " +
                    "@annotation(exceptionHandler) && " +
                    "execution(* *(..))"
    )
    public void controller(ResponseExceptionHandler exceptionHandler) {
    }

    @Around("controller(exceptionHandler)")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint, ResponseExceptionHandler exceptionHandler) {
        ResponseDTO responseDTO;

        try {
            ResponseEntity response = (ResponseEntity) proceedingJoinPoint.proceed();
            if (!(response.getBody() instanceof ResponseDTO)) {
                responseDTO = new ResponseDTO();
                responseDTO.setResult(response.getBody());
                responseDTO.setStatus(ResponseStatus.SUCCESS);
                return new ResponseEntity(responseDTO, HttpStatus.OK);
            }

            return response;

        } catch (Throwable e) {
            LOG.warn("START: Response failed, exception is {}", e);

            ResponseEntity responseEntity;
            responseDTO = new ResponseDTO();

            if (e instanceof BaseException) {

                BaseException baseException = (BaseException) e;
                responseDTO.setAppsErrorMessages(
                        this.exceptionMessageFinder.getMessages(baseException.getAppsErrorMessages()));
                responseDTO.setStatus(baseException.getResponseStatus());
                responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.valueOf(baseException.getHttpStatus()));
                LOG.warn("Response failed, Exception is handled in code {}", baseException);

            } else if (e instanceof AccessDeniedException) {

                AppsErrorMessage appsErrorMessage = new AppsErrorMessage();
                appsErrorMessage.setErrorCode(AppsCommonErrorCode.APPS_UNAUTHORISED);
                appsErrorMessage.setErrorMessage(e.getMessage());

                List<AppsErrorMessage> errorMessages = new ArrayList<>();
                errorMessages.add(appsErrorMessage);
                responseDTO.setAppsErrorMessages(errorMessages);

                responseDTO.setStatus(ResponseStatus.FAILED);
                responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);

                LOG.warn("User is unauthorized for action ", e);
            } else {

                AppsErrorMessage appsErrorMessage = new AppsErrorMessage();
                appsErrorMessage.setErrorCode(AppsCommonErrorCode.APPS_DEFAULT_ERROR);
                appsErrorMessage.setErrorMessage(
                        this.exceptionMessageFinder.getMessage(AppsCommonErrorCode.APPS_DEFAULT_ERROR));

                List<AppsErrorMessage> errorMessages = new ArrayList<>();
                errorMessages.add(appsErrorMessage);
                responseDTO.setAppsErrorMessages(errorMessages);

                responseDTO.setStatus(ResponseStatus.FAILED);

                responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
                LOG.error("Response failed, Exception is not handled in code {}", e);

            }

            LOG.info("END: Request failed, response is : {}", responseEntity);
            return responseEntity;
        }
    }
}
