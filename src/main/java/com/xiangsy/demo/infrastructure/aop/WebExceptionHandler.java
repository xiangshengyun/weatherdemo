package com.xiangsy.demo.infrastructure.aop;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.xiangsy.demo.common.ErrorData;
import com.xiangsy.demo.infrastructure.exception.BizException;

import lombok.RequiredArgsConstructor;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 */
@RequiredArgsConstructor
@ControllerAdvice("com.xiangsy.demo")
public class WebExceptionHandler {
    private static final String MSG_INVALID = "{\"key\": KEY_HOLDER,\"message\":\"Invalid error message!\"}";
    public static final String SERVER_INTERNAL_ERROR = "server.internal.error";
    private final MessageSource msgSource;

    /**
     * handleGeneralException
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(HttpServletRequest request, Exception ex) {
        String key = SERVER_INTERNAL_ERROR;
        String msg = getMessage(key, LocaleContextHolder.getLocale());
        return generateResponse(key, msg, null, 500);
    }

    /**
     * handleBizException
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<Object> handleApiException(BizException ex) {
        String msg = getMessage(ex.getErrorKey(), LocaleContextHolder.getLocale());
        return generateResponse(ex.getErrorKey(), msg, ex.getValues(), 400);
    }

    private ResponseEntity<Object> generateResponse(String key, String msg, Object[] args, int status) {
        ErrorData errorData = new ErrorData();
        errorData.setKey(key);
        errorData.setMessage(msg);
        if (args != null && args.length > 0) {
            errorData.setData(args);
        }
        return new ResponseEntity<>(errorData, HttpStatus.valueOf(status));
    }

    private String getMessage(String msgKey, Locale locale) {
        String msg;
        try {
            msg = msgSource.getMessage(msgKey, null, locale);
        } catch (Exception e) {
            msg = MSG_INVALID.replace("KEY_HOLDER", msgKey);
        }
        return msg;
    }
}
