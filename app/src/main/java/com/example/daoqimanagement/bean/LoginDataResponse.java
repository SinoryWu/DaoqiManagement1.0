package com.example.daoqimanagement.bean;

public class LoginDataResponse {
    @Override
    public String toString() {
        return "LoginDataResponse{" +
                "token='" + token + '\'' +
                ", uid=" + uid +
                ", userType=" + userType +
                ", supply=" + supply +
                ", payment=" + payment +
                '}';
    }

    /**
     * token : 7e70f3505ff6d6c8c636461308343b55
     * uid : 12
     * userType : 2
     * supply : true
     * payment : true
     */


    private String token;
    private int uid;
    private int userType;
    private boolean supply;
    private int payment;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public boolean isSupply() {
        return supply;
    }

    public void setSupply(boolean supply) {
        this.supply = supply;
    }

    public int isPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}

