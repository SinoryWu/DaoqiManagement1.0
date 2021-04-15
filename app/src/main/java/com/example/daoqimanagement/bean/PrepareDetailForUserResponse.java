package com.example.daoqimanagement.bean;

import java.util.List;

public class PrepareDetailForUserResponse {


    /**
     * code : 0
     * data : {"productId":1,"hospitalId":1,"voucher":"/upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg","protectTime":"2021-10-14 14:33:28","status":90,"productName":"八戒睡眠-设备投放","department":"停尸房","reason":"生命的归宿","hospitalName":"杭州市第一人民医院","level":"三级","nature":"公立","areaName":"浙江省杭州市西湖区","delay":1,"payProtect":0,"payAmount":"","scheduleList":[{"scheduleId":3,"desc":"我是描述","createdTime":"2021-02-26 16:39:10","truename":"荔枝面"}]}
     * msg : ok
     */

    private int code;
    private DataBean data;
    private String msg;

    @Override
    public String toString() {
        return "PrepareDetailForUserResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

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
                    "productId=" + productId +
                    ", hospitalId=" + hospitalId +
                    ", voucher='" + voucher + '\'' +
                    ", protectTime='" + protectTime + '\'' +
                    ", status=" + status +
                    ", productName='" + productName + '\'' +
                    ", department='" + department + '\'' +
                    ", reason='" + reason + '\'' +
                    ", hospitalName='" + hospitalName + '\'' +
                    ", level='" + level + '\'' +
                    ", nature='" + nature + '\'' +
                    ", areaName='" + areaName + '\'' +
                    ", delay=" + delay +
                    ", payProtect=" + payProtect +
                    ", payAmount='" + payAmount + '\'' +
                    ", scheduleList=" + scheduleList +
                    '}';
        }

        /**
         * productId : 1
         * hospitalId : 1
         * voucher : /upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg
         * protectTime : 2021-10-14 14:33:28
         * status : 90
         * productName : 八戒睡眠-设备投放
         * department : 停尸房
         * reason : 生命的归宿
         * hospitalName : 杭州市第一人民医院
         * level : 三级
         * nature : 公立
         * areaName : 浙江省杭州市西湖区
         * delay : 1
         * payProtect : 0
         * payAmount :
         * scheduleList : [{"scheduleId":3,"desc":"我是描述","createdTime":"2021-02-26 16:39:10","truename":"荔枝面"}]
         */



        private int productId;
        private int hospitalId;
        private String voucher;
        private String protectTime;
        private int status;
        private String productName;
        private String department;
        private String reason;
        private String hospitalName;
        private String level;
        private String nature;
        private String areaName;
        private int delay;
        private int payProtect;
        private String payAmount;
        private List<ScheduleListBean> scheduleList;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getVoucher() {
            return voucher;
        }

        public void setVoucher(String voucher) {
            this.voucher = voucher;
        }

        public String getProtectTime() {
            return protectTime;
        }

        public void setProtectTime(String protectTime) {
            this.protectTime = protectTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public int getPayProtect() {
            return payProtect;
        }

        public void setPayProtect(int payProtect) {
            this.payProtect = payProtect;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public List<ScheduleListBean> getScheduleList() {
            return scheduleList;
        }

        public void setScheduleList(List<ScheduleListBean> scheduleList) {
            this.scheduleList = scheduleList;
        }

        public static class ScheduleListBean {
            @Override
            public String toString() {
                return "ScheduleListBean{" +
                        "scheduleId=" + scheduleId +
                        ", desc='" + desc + '\'' +
                        ", createdTime='" + createdTime + '\'' +
                        ", truename='" + truename + '\'' +
                        '}';
            }

            /**
             * scheduleId : 3
             * desc : 我是描述
             * createdTime : 2021-02-26 16:39:10
             * truename : 荔枝面
             */


            private int scheduleId;
            private String desc;
            private String createdTime;
            private String truename;

            public int getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(int scheduleId) {
                this.scheduleId = scheduleId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getTruename() {
                return truename;
            }

            public void setTruename(String truename) {
                this.truename = truename;
            }
        }
    }
}
