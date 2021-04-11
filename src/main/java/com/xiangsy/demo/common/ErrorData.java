package com.xiangsy.demo.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 */
@Data
@NoArgsConstructor
public class ErrorData {
    private String key;
    private String message;
    private Object data;
}
