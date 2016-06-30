package com.techjumper.polyhomeb.entity.tcp_udp;

import java.io.Serializable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class BaseReceiveEntity<T> implements Serializable {

    public static final String CODE_SUCCESS = "0";
    public static final String MSG_RECEIVE_OK = "ReciveOK";

    private T data;

    private String msg;
    private String code;
    private String method;
    private String user_id;

    private String senceid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSenceid() {
        return senceid;
    }

    public void setSenceid(String senceid) {
        this.senceid = senceid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseReceiveEntity{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", method='" + method + '\'' +
                ", senceid='" + senceid + '\'' +
                '}';
    }
}
