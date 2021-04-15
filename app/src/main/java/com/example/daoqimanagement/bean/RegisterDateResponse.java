package com.example.daoqimanagement.bean;

public class RegisterDateResponse {
    @Override
    public String toString() {
        return "RegisterDateResponse{" +
                "token='" + token + '\'' +
                ", uid=" + uid +
                ", userType='" + userType + '\'' +
                '}';
    }

    /**
     * token : 1814eec4b01029bf2c71a0de0fd7f264
     * uid : 12
     * userType : 3
     */


    private String token;
    private int uid;
    private String userType;

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

