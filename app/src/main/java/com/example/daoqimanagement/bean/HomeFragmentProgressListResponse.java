package com.example.daoqimanagement.bean;

import java.util.List;

public class HomeFragmentProgressListResponse {


    @Override
    public String toString() {
        return "HomeFragmentProgressListResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"current_page":1,"data":[{"scheduleId":21,"created_at":"2021-03-02 13:07:37","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":20,"created_at":"2021-03-02 13:07:36","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":19,"created_at":"2021-03-02 13:07:36","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":18,"created_at":"2021-03-02 13:07:35","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":17,"created_at":"2021-03-02 13:07:34","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":16,"created_at":"2021-03-02 13:07:33","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":15,"created_at":"2021-03-02 13:07:33","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":14,"created_at":"2021-03-02 13:07:32","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":13,"created_at":"2021-03-02 13:07:32","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":12,"created_at":"2021-03-02 13:07:31","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":11,"created_at":"2021-03-02 13:07:30","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":10,"created_at":"2021-03-02 13:07:29","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":9,"created_at":"2021-03-02 13:07:29","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":8,"created_at":"2021-03-02 13:07:28","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":7,"created_at":"2021-03-02 13:07:27","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"}],"first_page_url":"http://47.98.205.142:9510/v1/schedule/list?page=1","from":1,"last_page":2,"last_page_url":"http://47.98.205.142:9510/v1/schedule/list?page=2","next_page_url":"http://47.98.205.142:9510/v1/schedule/list?page=2","path":"http://47.98.205.142:9510/v1/schedule/list","per_page":15,"prev_page_url":null,"to":15,"total":19}
     * msg : ok
     */


    private int code;
    private DataBeanX data;
    private String msg;

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
                    ", next_page_url='" + next_page_url + '\'' +
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
         * data : [{"scheduleId":21,"created_at":"2021-03-02 13:07:37","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":20,"created_at":"2021-03-02 13:07:36","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":19,"created_at":"2021-03-02 13:07:36","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":18,"created_at":"2021-03-02 13:07:35","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":17,"created_at":"2021-03-02 13:07:34","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":16,"created_at":"2021-03-02 13:07:33","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":15,"created_at":"2021-03-02 13:07:33","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":14,"created_at":"2021-03-02 13:07:32","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":13,"created_at":"2021-03-02 13:07:32","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":12,"created_at":"2021-03-02 13:07:31","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":11,"created_at":"2021-03-02 13:07:30","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":10,"created_at":"2021-03-02 13:07:29","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":9,"created_at":"2021-03-02 13:07:29","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":8,"created_at":"2021-03-02 13:07:28","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"},{"scheduleId":7,"created_at":"2021-03-02 13:07:27","hospitalName":"杭州市第一人民医院","truename":"荔枝面","desc":"我是描述"}]
         * first_page_url : http://47.98.205.142:9510/v1/schedule/list?page=1
         * from : 1
         * last_page : 2
         * last_page_url : http://47.98.205.142:9510/v1/schedule/list?page=2
         * next_page_url : http://47.98.205.142:9510/v1/schedule/list?page=2
         * path : http://47.98.205.142:9510/v1/schedule/list
         * per_page : 15
         * prev_page_url : null
         * to : 15
         * total : 19
         */



        private int current_page;
        private String first_page_url;
        private int from;
        private int last_page;
        private String last_page_url;
        private String next_page_url;
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

        public String getNext_page_url() {
            return next_page_url;
        }

        public void setNext_page_url(String next_page_url) {
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
            @Override
            public String toString() {
                return "DataBean{" +
                        "scheduleId=" + scheduleId +
                        ", created_at='" + created_at + '\'' +
                        ", hospitalName='" + hospitalName + '\'' +
                        ", truename='" + truename + '\'' +
                        ", desc='" + desc + '\'' +
                        ", current_page=" + current_page +
                        ", last_page=" + last_page +
                        '}';
            }

            /**
             * scheduleId : 21
             * created_at : 2021-03-02 13:07:37
             * hospitalName : 杭州市第一人民医院
             * truename : 荔枝面
             * desc : 我是描述
             */



            private int scheduleId;
            private String created_at;
            private String hospitalName;
            private String truename;
            private String desc;
            private int current_page;
            private int last_page;
            public int getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(int current_page) {
                this.current_page = current_page;
            }
            public void setLast_page(int last_page) {
                this.last_page = last_page;
            }
            public int getLast_page() {
                return last_page;
            }
            public int getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(int scheduleId) {
                this.scheduleId = scheduleId;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
