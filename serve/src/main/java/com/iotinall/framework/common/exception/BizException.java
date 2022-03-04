package com.iotinall.framework.common.exception;

public class BizException extends RuntimeException{
    private final String code;
    private final String msg;

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String toString() {
        return "BizException{code='" + this.code + '\'' + ", msg='" + this.msg + '\'' + '}';
    }
}
