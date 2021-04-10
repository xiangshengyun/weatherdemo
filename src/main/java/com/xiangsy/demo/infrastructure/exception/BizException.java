package com.xiangsy.demo.infrastructure.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: xiangsy
 * @version: 2021-04-09 22:24
 * @description: customized runtime exception
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -2384766678062059421L;
    private final String errorKey;
    private final String[] values;

    public BizException(final String errorKey, final String... values) {
        this.errorKey = errorKey;
        this.values = values;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String[] getValues() {
        return values;
    }

    public String getErrorKeyAndValues() {
        StringBuilder sb = new StringBuilder(errorKey);
        sb.append(": ").append(StringUtils.join(values, ","));
        return sb.toString();
    }
}
