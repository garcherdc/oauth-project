package com.example.demo.exception;


import com.example.demo.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -2096728164166219382L;

    @Getter
    private int errCode;
    @Getter
    private String errMsg;
    @Getter
    private boolean jointCodeMsg = false;
    @Getter
    private final ResultCodeEnum codeEnum;


    public BizException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.errCode = codeEnum.getCode();
        this.codeEnum = codeEnum;
    }

    public BizException(ResultCodeEnum codeEnum, String errMsg) {
        super(errMsg);
        this.errCode = codeEnum.getCode();
        this.errMsg = errMsg;
        this.codeEnum = codeEnum;
    }

    public BizException(ResultCodeEnum codeEnum, String errorField, boolean jointCodeMsg) {
        super(codeEnum.getMessage());
        this.errCode = codeEnum.getCode();
        this.errMsg = errorField;
        this.codeEnum = codeEnum;
        this.jointCodeMsg = jointCodeMsg;
    }
}
