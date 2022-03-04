package com.iotinall.framework.common.model;

public class ResultDTO<T> {
    private String code;
    private String msg;
    private T data;

    private ResultDTO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultDTO<T> success(T data, String msg) {
        return new ResultDTO("0", msg, data);
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO("0", null, data);
    }

    public static <T> ResultDTO<T> success() {
        return new ResultDTO("0", null, (Object)null);
    }

    public static <T> ResultDTO<T> failed(String code, String msg) {
        return new ResultDTO(code, msg, null);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "ResultDTO{code='" + this.code + '\'' + ", msg='" + this.msg + '\'' + ", data=" + this.data + '}';
    }
}
