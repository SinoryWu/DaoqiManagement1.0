package com.example.daoqimanagement.bean;

import java.util.List;

public class SearchHospitalListResponse {


    /**
     * code : 0
     * data : {"current_page":1,"data":[{"headPic":"","status":1,"hospitalid":2,"hospitalName":"滨医附院","detail":"这是一所位于滨州的医院","level":"三级甲等","nature":"公立","areaName":"山东省滨州市滨城区"}],"first_page_url":"http://47.98.205.142:9510/v1/hospital/list?page=1","from":1,"last_page":2,"last_page_url":"http://47.98.205.142:9510/v1/hospital/list?page=2","next_page_url":"http://47.98.205.142:9510/v1/hospital/list?page=2","path":"http://47.98.205.142:9510/v1/hospital/list","per_page":1,"prev_page_url":null,"to":1,"total":2}
     * msg : ok
     */

    private int code;
    private DataBeanX data;
    private String msg;

    @Override
    public String toString() {
        return "SearchHospitalListResponse{" +
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
         * data : [{"headPic":"","status":1,"hospitalid":2,"hospitalName":"滨医附院","detail":"这是一所位于滨州的医院","level":"三级甲等","nature":"公立","areaName":"山东省滨州市滨城区"}]
         * first_page_url : http://47.98.205.142:9510/v1/hospital/list?page=1
         * from : 1
         * last_page : 2
         * last_page_url : http://47.98.205.142:9510/v1/hospital/list?page=2
         * next_page_url : http://47.98.205.142:9510/v1/hospital/list?page=2
         * path : http://47.98.205.142:9510/v1/hospital/list
         * per_page : 1
         * prev_page_url : null
         * to : 1
         * total : 2
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
                        "headPic='" + headPic + '\'' +
                        ", status=" + status +
                        ", hospitalid=" + hospitalid +
                        ", hospitalName='" + hospitalName + '\'' +
                        ", detail='" + detail + '\'' +
                        ", level='" + level + '\'' +
                        ", nature='" + nature + '\'' +
                        ", areaName='" + areaName + '\'' +
                        '}';
            }

            /**
             * headPic :
             * status : 1
             * hospitalid : 2
             * hospitalName : 滨医附院
             * detail : 这是一所位于滨州的医院
             * level : 三级甲等
             * nature : 公立
             * areaName : 山东省滨州市滨城区
             */



            private String headPic;
            private int status;
            private int hospitalid;
            private String hospitalName;
            private String detail;
            private String level;
            private String nature;
            private String areaName;

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getHospitalid() {
                return hospitalid;
            }

            public void setHospitalid(int hospitalid) {
                this.hospitalid = hospitalid;
            }

            public String getHospitalName() {
                return hospitalName;
            }

            public void setHospitalName(String hospitalName) {
                this.hospitalName = hospitalName;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
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
        }
    }
}
