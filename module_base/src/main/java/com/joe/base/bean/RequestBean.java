package com.joe.base.bean;

/**
 * @author Joe
 * @time 7/17/2018 9:27 AM
 */
public class RequestBean<O> {
    private boolean success;
    private int status;
    private String msg;
    private O obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public O getObj() {
        return obj;
    }

    public void setObj(O obj) {
        this.obj = obj;
    }
}
