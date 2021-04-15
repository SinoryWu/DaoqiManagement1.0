package com.example.daoqimanagement.bean;

public class EditPartnerResponse {
    @Override
    public String toString() {
        return "AddPrepareResponse{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : 操作成功
     * msg : ok
     */



    private int code;
    private String data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
