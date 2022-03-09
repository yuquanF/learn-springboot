package com.f.missyou.core;

import com.f.missyou.core.configuration.ExceptionCodeConfiguration;
import com.f.missyou.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionAdvice {
    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    @Value("${env}")
    private String env;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        String request = getRequest(req);
        int code = 9999;
        if (env.equals("dev")) {
            System.out.println(e);
        }
        return new UnifyResponse(code, exceptionCodeConfiguration.getMessage(code), request);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        String request = getRequest(req);
        UnifyResponse unifyResponse = new UnifyResponse(e.getCode(), exceptionCodeConfiguration.getMessage(e.getCode()), request);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        ResponseEntity<UnifyResponse> responseEntity = new ResponseEntity(unifyResponse, httpHeaders, httpStatus);
        return responseEntity;
    }

    //参数校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UnifyResponse handleBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String message = formatAllErrorMessages(errors);
        String request = getRequest(req);

        return new UnifyResponse(10001, message, request);
    }


    //参数校验
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UnifyResponse handleConstrainException(HttpServletRequest req, ConstraintViolationException e) {
//        String message = e.getMessage();
        StringBuilder message = new StringBuilder();
        String request = getRequest(req);
        message.append("【").append(e.getConstraintViolations().size()).append("】"); // 添加错误的数量，方便查看
        for (ConstraintViolation<?> error : e.getConstraintViolations()) {
            String[] arr = error.getPropertyPath().toString().split("\\.");
            String target = arr[arr.length - 1];
            String msg = error.getMessage();
            message.append(target).append(":").append(msg).append("; ");
        }

        return new UnifyResponse(10001, message.toString().trim(), request);
    }

    private String getRequest(HttpServletRequest req) {
        String method = req.getMethod();
        String requestUrl = req.getRequestURI();
        return method + " " + requestUrl;
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuffer errorMsg = new StringBuffer();
        errorMsg.append("【").append(errors.size()).append("】"); // 添加错误的数量，方便查看
        errors.forEach(error -> {
            String target = ((DefaultMessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0])
                    .getDefaultMessage();
            if (!"".equals(target)) {
                errorMsg.append(target).append(":");
            }
            errorMsg.append(error.getDefaultMessage()).append("; ");
        });
        return errorMsg.toString().trim();
    }
}
