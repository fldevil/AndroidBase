package com.user.project.domain;

/**
 * Created by gg on 2017/2/27.
 * http异常返回
 */
public class HttpError extends BaseObj {

    private int errorCode;
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
