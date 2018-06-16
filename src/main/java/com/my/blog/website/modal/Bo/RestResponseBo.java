package com.my.blog.website.modal.Bo;

/**
 * rest返回对象
 *^
 * @param <T>
 */
public class RestResponseBo<T> {

    /**
     * 服务器响应数据
     */
    private T payload;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    /**
     * 无参构造器
     */
    public RestResponseBo() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    /**
     * 有参构造器
     * @param success 请求是否成功
     */
    public RestResponseBo(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000; // 初始化响应时间
        this.success = success;
    }

    /**
     *
     * @param success 请求是否成功
     * @param payload 服务器响应数据
     */
    public RestResponseBo(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    /**
     * @param success 请求是否成功
     * @param payload 服务器响应数据
     * @param code 状态码
     */
    public RestResponseBo(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    /**
     * @param success 请求是否成功
     * @param msg 错误信息
     */
    public RestResponseBo(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    /**
     * @param success 请求是否成功
     * @param msg 错误信息
     * @param code 状态码
     */
    public RestResponseBo(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static RestResponseBo ok() {
        return new RestResponseBo(true);
    }

    public static <T> RestResponseBo ok(T payload) {
        return new RestResponseBo(true, payload);
    }

    public static <T> RestResponseBo ok(int code) {
        return new RestResponseBo(true, null, code);
    }

    public static <T> RestResponseBo ok(T payload, int code) {
        return new RestResponseBo(true, payload, code);
    }

    public static RestResponseBo fail() {
        return new RestResponseBo(false);
    }

    public static RestResponseBo fail(String msg) {
        return new RestResponseBo(false, msg);
    }

    public static RestResponseBo fail(int code) {
        return new RestResponseBo(false, null, code);
    }

    public static RestResponseBo fail(int code, String msg) {
        return new RestResponseBo(false, msg, code);
    }

}