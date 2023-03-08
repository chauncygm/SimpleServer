package com.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应文本
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * response success result wrapper.
     */
    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder().data(data)
                .message(ResponseStatus.SUCCESS.getMessage())
                .status(ResponseStatus.SUCCESS.getCode())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * response error result wrapper.
     */
    public static <T extends Serializable> ResponseResult<T> fail(String message) {
        return fail(null, message);
    }

    /**
     * response error result wrapper.
     */
    public static <T> ResponseResult<T> fail(T data, String message) {
        return ResponseResult.<T>builder().data(data)
                .message(message)
                .status(ResponseStatus.FAIL.getCode())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 创建一个新的响应构建器
     */
    private static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * 响应构建器
     *
     * @param <T> 响应数据类型
     */
    private static class Builder<T> {

        private final ResponseResult<T> result = new ResponseResult<>();

        private Builder<T> data(T data) {
            result.setData(data);
            return this;
        }

        private Builder<T> message(String message) {
            result.setMsg(message);
            return this;
        }

        private Builder<T> status(int code) {
            return this;
        }

        private Builder<T> timestamp(long milli) {
            result.setTimestamp(milli);
            return this;
        }

        private ResponseResult<T> build() {
            return result;
        }

    }
}
