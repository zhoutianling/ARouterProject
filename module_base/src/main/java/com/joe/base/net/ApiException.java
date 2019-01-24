package com.joe.base.net;

/**
 * @author Joe
 * @time 11/2/2018 10:09 AM
 */
public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
