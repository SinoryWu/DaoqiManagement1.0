package com.example.daoqimanagement.bean;

import java.util.List;

public class ApprovalLogResponse {

    @Override
    public String toString() {
        return "ApprovalLogResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * code : 0
     * data : [{"truename":"终审","userType":7,"status":2,"updatedAt":"2021-03-19 13:20:21","opinion":"可以的，看好你【复审】"},{"truename":"复审","userType":6,"status":2,"updatedAt":"2021-03-19 13:20:16","opinion":"可以的，看好你【复审】"},{"truename":"初审","userType":5,"status":2,"updatedAt":"2021-03-19 13:20:04","opinion":"可以的，看好你【我是初审】"},{"truename":"财务","userType":4,"status":2,"updatedAt":"2021-03-22 11:39:46","opinion":"可以的，看好1你【我是财务】"},{"updatedAt":"2021-02-26 13:58:12","truename":"荔枝面","userType":3,"status":0,"opinion":""}]
     * msg : ok
     */


    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "truename='" + truename + '\'' +
                    ", userType=" + userType +
                    ", status=" + status +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", opinion='" + opinion + '\'' +
                    '}';
        }

        /**
         * truename : 终审
         * userType : 7
         * status : 2
         * updatedAt : 2021-03-19 13:20:21
         * opinion : 可以的，看好你【复审】
         */


        private String truename;
        private int userType;
        private int status;
        private String updatedAt;
        private String opinion;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }
    }
}
