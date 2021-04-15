package com.example.daoqimanagement.bean;

import java.util.List;

public class MentorsFragmentAdminTeamListResponse {

    @Override
    public String toString() {
        return "MentorsFragmentAdminTeamListResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"current_page":1,"data":[{"truename":"荔枝面","uid":49,"mobile":"18969039923","userType":3,"createTime":"2021-03-04 11:09:21"},{"truename":"荔枝面9","uid":9,"mobile":"18969039914","userType":3,"createTime":"2021-02-19 15:12:00"},{"truename":"荔枝面8","uid":8,"mobile":"18969039916","userType":3,"createTime":"2021-02-19 15:15:55"}],"first_page_url":"http://47.98.205.142:9510/v1/team/teamList?page=1","from":1,"last_page":1,"last_page_url":"http://47.98.205.142:9510/v1/team/teamList?page=1","next_page_url":null,"path":"http://47.98.205.142:9510/v1/team/teamList","per_page":15,"prev_page_url":null,"to":3,"total":3}
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
         * data : [{"truename":"荔枝面","uid":49,"mobile":"18969039923","userType":3,"createTime":"2021-03-04 11:09:21"},{"truename":"荔枝面9","uid":9,"mobile":"18969039914","userType":3,"createTime":"2021-02-19 15:12:00"},{"truename":"荔枝面8","uid":8,"mobile":"18969039916","userType":3,"createTime":"2021-02-19 15:15:55"}]
         * first_page_url : http://47.98.205.142:9510/v1/team/teamList?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://47.98.205.142:9510/v1/team/teamList?page=1
         * next_page_url : null
         * path : http://47.98.205.142:9510/v1/team/teamList
         * per_page : 15
         * prev_page_url : null
         * to : 3
         * total : 3
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
            @Override
            public String toString() {
                return "DataBean{" +
                        "truename='" + truename + '\'' +
                        ", headPic='" + headPic + '\'' +
                        ", uid=" + uid +
                        ", mobile='" + mobile + '\'' +
                        ", userType=" + userType +
                        ", createTime='" + createTime + '\'' +
                        ", current_page=" + current_page +
                        ", last_page=" + last_page +
                        '}';
            }

            /**
             * truename : 荔枝面
             * uid : 49
             * mobile : 18969039923
             * userType : 3
             * createTime : 2021-03-04 11:09:21
             */

            private String truename;
            private String headPic;
            private int uid;
            private String mobile;
            private int userType;
            private String createTime;
            private int current_page;
            private int last_page;

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getLast_page() {
                return last_page;
            }

            public void setLast_page(int last_page) {
                this.last_page = last_page;
            }


            public int getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(int current_page) {
                this.current_page = current_page;
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

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
