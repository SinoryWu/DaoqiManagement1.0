package com.example.daoqimanagement.bean;

import java.util.List;

public class PartnerTeamResponse {

    @Override
    public String toString() {
        return "PartnerTeamResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * code : 0
     * data : [{"mobile":"18969039922","position":"营养师","truename":"运营","uid":28,"headPic":""}]
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
                    "mobile='" + mobile + '\'' +
                    ", position='" + position + '\'' +
                    ", truename='" + truename + '\'' +
                    ", uid=" + uid +
                    ", headPic='" + headPic + '\'' +
                    '}';
        }

        /**
         * mobile : 18969039922
         * position : 营养师
         * truename : 运营
         * uid : 28
         * headPic :
         */


        private String mobile;
        private String position;
        private String truename;
        private int uid;
        private String headPic;

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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }
    }
}
