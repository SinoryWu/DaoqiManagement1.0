package com.example.daoqimanagement.bean;

public class LoginResponse {


    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"token":"7e70f3505ff6d6c8c636461308343b55","uid":12,"userType":2,"supply":true,"payment":true}
     * msg : ok
     */

    private int code;
    private Object data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
