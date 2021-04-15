package com.example.daoqimanagement.bean;

import java.util.List;

public class PartnerPrepareListResponse {


    /**
     * code : 0
     * data : {"current_page":1,"data":[{"hospitalid":1,"protectTime":null,"prepareId":20,"status":1,"productName":"八戒睡眠-购买设备","hospitalName":"杭州市第一人民医院","truename":"荔枝面","hospitalPic":"/upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg","scheduleNum":0}],"first_page_url":"http://47.98.205.142:9510/v1/team/prepareList?page=1","from":1,"last_page":1,"last_page_url":"http://47.98.205.142:9510/v1/team/prepareList?page=1","next_page_url":null,"path":"http://47.98.205.142:9510/v1/team/prepareList","per_page":15,"prev_page_url":null,"to":1,"total":1}
     * msg : ok
     */

    private int code;
    private DataBeanX data;
    private String msg;

    @Override
    public String toString() {
        return "PartnerPrepareListResponse{" +
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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        @Override
        public String toString() {
            return "DataBeanX{" +
                    "current_page=" + current_page +
                    ", first_page_url='" + first_page_url + '\'' +
                    ", from=" + from +
                    ", last_page=" + last_page +
                    ", last_page_url='" + last_page_url + '\'' +
                    ", next_page_url=" + next_page_url +
                    ", path='" + path + '\'' +
                    ", per_page=" + per_page +
                    ", prev_page_url=" + prev_page_url +
                    ", to=" + to +
                    ", total=" + total +
                    ", data=" + data +
                    '}';
        }

        /**
         * current_page : 1
         * data : [{"hospitalid":1,"protectTime":null,"prepareId":20,"status":1,"productName":"八戒睡眠-购买设备","hospitalName":"杭州市第一人民医院","truename":"荔枝面","hospitalPic":"/upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg","scheduleNum":0}]
         * first_page_url : http://47.98.205.142:9510/v1/team/prepareList?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://47.98.205.142:9510/v1/team/prepareList?page=1
         * next_page_url : null
         * path : http://47.98.205.142:9510/v1/team/prepareList
         * per_page : 15
         * prev_page_url : null
         * to : 1
         * total : 1
         */


        private int current_page;
        private String first_page_url;
        private int from;
        private int last_page;
        private String last_page_url;
        private Object next_page_url;
        private String path;
        private int per_page;
        private Object prev_page_url;
        private int to;
        private int total;
        private List<DataBean> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public String getFirst_page_url() {
            return first_page_url;
        }

        public void setFirst_page_url(String first_page_url) {
            this.first_page_url = first_page_url;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getLast_page_url() {
            return last_page_url;
        }

        public void setLast_page_url(String last_page_url) {
            this.last_page_url = last_page_url;
        }

        public Object getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(Object next_page_url) {
            this.next_page_url = next_page_url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public Object getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(Object prev_page_url) {
            this.prev_page_url = prev_page_url;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * hospitalid : 1
             * protectTime : null
             * prepareId : 20
             * status : 1
             * productName : 八戒睡眠-购买设备
             * hospitalName : 杭州市第一人民医院
             * truename : 荔枝面
             * hospitalPic : /upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg
             * scheduleNum : 0
             */

            private int hospitalid;
            private String protectTime;
            private int prepareId;
            private int status;
            private String productName;
            private String hospitalName;
            private String truename;
            private String hospitalPic;
            private int scheduleNum;

            @Override
            public String toString() {
                return "DataBean{" +
                        "hospitalid=" + hospitalid +
                        ", protectTime=" + protectTime +
                        ", prepareId=" + prepareId +
                        ", status=" + status +
                        ", productName='" + productName + '\'' +
                        ", hospitalName='" + hospitalName + '\'' +
                        ", truename='" + truename + '\'' +
                        ", hospitalPic='" + hospitalPic + '\'' +
                        ", scheduleNum=" + scheduleNum +
                        '}';
            }

            public int getHospitalid() {
                return hospitalid;
            }

            public void setHospitalid(int hospitalid) {
                this.hospitalid = hospitalid;
            }

            public String getProtectTime() {
                return protectTime;
            }

            public void setProtectTime(String protectTime) {
                this.protectTime = protectTime;
            }

            public int getPrepareId() {
                return prepareId;
            }

            public void setPrepareId(int prepareId) {
                this.prepareId = prepareId;
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

            public String getHospitalName() {
                return hospitalName;
            }

            public void setHospitalName(String hospitalName) {
                this.hospitalName = hospitalName;
            }

            public String getTruename() {
                return truename;
            }

            public void setTruename(String truename) {
                this.truename = truename;
            }

            public String getHospitalPic() {
                return hospitalPic;
            }

            public void setHospitalPic(String hospitalPic) {
                this.hospitalPic = hospitalPic;
            }

            public int getScheduleNum() {
                return scheduleNum;
            }

            public void setScheduleNum(int scheduleNum) {
                this.scheduleNum = scheduleNum;
            }
        }
    }
}
