package com.example.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespVO<T> implements Serializable {

    private static final long serialVersionUID = 5051477856632360910L;

    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

    /**
     * 响应正文
     */
    private T data;

    /**
     * 是否成功
     */
    private boolean success;
}
