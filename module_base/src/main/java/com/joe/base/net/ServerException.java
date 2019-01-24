package com.joe.base.net;

/**
 * @author Joe
 * @time 11/2/2018 10:12 AM
 */
public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public ServerException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
