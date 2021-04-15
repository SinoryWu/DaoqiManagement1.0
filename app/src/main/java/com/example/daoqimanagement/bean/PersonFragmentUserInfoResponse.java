package com.example.daoqimanagement.bean;

public class PersonFragmentUserInfoResponse {

    @Override
    public String toString() {
        return "PersonFragmentUserInfoResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"authentication":true,"hospitalNum":2,"contract":2,"payment":1,"truename":"荔枝面8","userType":3}
     * msg : ok
     */


    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "authentication=" + authentication +
                    ", hospitalNum=" + hospitalNum +
                    ", contract=" + contract +
                    ", payment=" + payment +
                    ", truename='" + truename + '\'' +
                    ", userType=" + userType +
                    '}';
        }

        /**
         * authentication : true
         * hospitalNum : 2
         * contract : 2
         * payment : 1
         * truename : 荔枝面8
         * userType : 3
         */


        private boolean authentication;
        private int hospitalNum;
        private int contract;
        private int payment;
        private String truename;
        private int userType;

        public boolean isAuthentication() {
            return authentication;
        }

        public void setAuthentication(boolean authentication) {
            this.authentication = authentication;
        }

        public int getHospitalNum() {
            return hospitalNum;
        }

        public void setHospitalNum(int hospitalNum) {
            this.hospitalNum = hospitalNum;
        }

        public int getContract() {
            return contract;
        }

        public void setContract(int contract) {
            this.contract = contract;
        }

        public int getPayment() {
            return payment;
        }

        public void setPayment(int payment) {
            this.payment = payment;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
