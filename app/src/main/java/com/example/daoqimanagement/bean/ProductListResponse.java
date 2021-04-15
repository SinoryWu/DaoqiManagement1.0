package com.example.daoqimanagement.bean;

import java.util.List;

public class ProductListResponse {

    @Override
    public String toString() {
        return "ProductListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * code : 0
     * data : [{"productid":1,"productName":"八戒睡眠-设备投放","introduce":"吃好喝好睡好"},{"productid":2,"productName":"八戒睡眠-购买设备","introduce":"买了睡得好"}]
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
                    "productid=" + productid +
                    ", productName='" + productName + '\'' +
                    ", introduce='" + introduce + '\'' +
                    '}';
        }

        /**
         * productid : 1
         * productName : 八戒睡眠-设备投放
         * introduce : 吃好喝好睡好
         */



        private int productid;
        private String productName;
        private String introduce;

        public int getProductid() {
            return productid;
        }

        public void setProductid(int productid) {
            this.productid = productid;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }
    }
}
