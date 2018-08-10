package com.learn.springcloud.auth.util;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 通知响应信息
 *
 * @author shunzhong
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SUCCESS_CODE = "200";

    public static final int SUCCESS_CODE_INT = 200;
    /**
     * 业务返回码
     */
    private String resCode;

    /**
     * 返回信息
     */
    private String resDesc;

    /**
     * 异常信息封装
     */
    private String exception;

    private T data;

    public Result() {
        this.resCode = SUCCESS_CODE;
    }

    /**
     * 新建个能直接赋值String类型code和desc的result
     *
     * @param resCode
     * @param Desc
     * @return
     */
    public static Result codeDesc(String resCode, String Desc) {
        Result result = new Result();
        result.setResCode(resCode);
        result.setResDesc(Desc);
        return result;
    }

    /**
     * 新建个能直接赋值String类型的code、desc、data的result
     *
     * @param resCode
     * @param Desc
     * @return
     */
    public static Result codeDescData(String resCode, String Desc, Object data) {
        Result result = new Result();
        result.setResCode(resCode);
        result.setResDesc(Desc);
        result.setData(data);
        return result;
    }


    /**
     * 返回响应的构造函数
     *
     * @param data 返回的数据
     */
    public Result(T data) {
        this.resCode = SUCCESS_CODE;
        this.data = data;
    }

    /**
     * 成功返回对应的构造函数
     *
     * @param resDesc 操作信息
     * @param data    对应的返回数据
     */
    public Result(String resDesc, T data) {
        this.resCode = SUCCESS_CODE;
        this.resDesc = resDesc;
        this.data = data;
    }

    public Result(int resCode, String resDesc, T data) {
        this.resCode = String.valueOf(resCode);
        this.resDesc = resDesc;
        this.data = data;
    }

    public Result(int resCode, String resDesc) {
        this.resCode = String.valueOf(resCode);
        this.resDesc = resDesc;
    }

    public Result(int resCode, String resDesc, String exception) {
        this.resCode = String.valueOf(resCode);
        this.resDesc = resDesc;
        this.exception = exception;
    }


    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setRetCode(int retCode) {
        this.resCode = String.valueOf(retCode);
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }


    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        if (exception.length() > 512) {
            exception = exception.substring(0, 512);
        }
        this.exception = exception;
    }


    @Override
    public String toString() {
        if (null == this) {
            return "";
        }
        return JSONObject.toJSONString(this);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
