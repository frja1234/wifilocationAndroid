package com.example.wifi.Model;

public class Internet {
    private String msg;
    private String code;
    private String data;

    @Override
    public String toString() {
        return "internet{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
