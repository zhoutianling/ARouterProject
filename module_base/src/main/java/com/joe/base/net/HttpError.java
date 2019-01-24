package com.joe.base.net;

/**
 * @author Joe
 * 约定异常
 * @time 11/2/2018 10:08 AM
 */
public class HttpError {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;
    /***
     * 响应超时
     */
    public static final int HTTP_TIMEOUT = 1004;
}
