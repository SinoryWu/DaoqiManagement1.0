package com.example.daoqimanagement.bean;

import java.util.List;

public class AreaListResponse {


    @Override
    public String toString() {
        return "AreaListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * code : 0
     * data : [{"areaId":922,"areaName":"上城区"},{"areaId":923,"areaName":"下城区"},{"areaId":924,"areaName":"江干区"},{"areaId":925,"areaName":"拱墅区"},{"areaId":926,"areaName":"西湖区"},{"areaId":927,"areaName":"滨江区"},{"areaId":928,"areaName":"萧山区"},{"areaId":929,"areaName":"余杭区"},{"areaId":930,"areaName":"富阳区"},{"areaId":931,"areaName":"桐庐县"},{"areaId":932,"areaName":"淳安县"},{"areaId":933,"areaName":"建德市"},{"areaId":934,"areaName":"临安市"}]
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
                    "areaId=" + areaId +
                    ", areaName='" + areaName + '\'' +
                    '}';
        }

        /**
         * areaId : 922
         * areaName : 上城区
         */


        private int areaId;
        private String areaName;

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }
}
