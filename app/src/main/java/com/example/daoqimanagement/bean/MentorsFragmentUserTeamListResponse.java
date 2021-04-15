package com.example.daoqimanagement.bean;

import java.util.List;

public class MentorsFragmentUserTeamListResponse {

    /**
     * code : 0
     * data : [{"mobile":"18969039922","position":"营养师","truename":"运营"},{"mobile":"18158188057","position":"药剂师","truename":"邬了"},{"mobile":"18158188058","position":"训练恢复师","truename":"岳灵珊"}]
     * msg : ok
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "MentorsFragmentUserTeamListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

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
                    "mobile='" + mobile + '\'' +
                    ", position='" + position + '\'' +
                    ", truename='" + truename + '\'' +
                    '}';
        }

        /**
         * mobile : 18969039922
         * position : 营养师
         * truename : 运营
         */


        private String mobile;
        private String position;
        private String truename;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }
    }
}
