package com.example.daoqimanagement.bean;

import java.util.List;

public class ResourceListResponse {

    /**
     * code : 0
     * data : {"current_page":1,"data":[{"title":"这里是标题","titleId":5,"desc":"","updatedAt":"2021-04-07 16:52:51","readNum":0,"type":2,"url":"http://wwdfasdfdf"},{"title":"这里是标题","titleId":4,"desc":"","updatedAt":"2021-04-06 13:39:52","readNum":0,"type":1,"url":""}],"first_page_url":"http://127.0.0.1:9510/v1/encyclopedia/list?page=1","from":1,"last_page":1,"last_page_url":"http://127.0.0.1:9510/v1/encyclopedia/list?page=1","next_page_url":null,"path":"http://127.0.0.1:9510/v1/encyclopedia/list","per_page":15,"prev_page_url":null,"to":2,"total":2}
     * msg : ok
     */

    private int code;
    private DataBeanX data;
    private String msg;

    @Override
    public String toString() {
        return "ResourceListResponse{" +
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
         * data : [{"title":"这里是标题","titleId":5,"desc":"","updatedAt":"2021-04-07 16:52:51","readNum":0,"type":2,"url":"http://wwdfasdfdf"},{"title":"这里是标题","titleId":4,"desc":"","updatedAt":"2021-04-06 13:39:52","readNum":0,"type":1,"url":""}]
         * first_page_url : http://127.0.0.1:9510/v1/encyclopedia/list?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://127.0.0.1:9510/v1/encyclopedia/list?page=1
         * next_page_url : null
         * path : http://127.0.0.1:9510/v1/encyclopedia/list
         * per_page : 15
         * prev_page_url : null
         * to : 2
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
        private String prev_page_url;
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

        public String getPrev_page_url() {
            return prev_page_url;
        }

        public void setPrev_page_url(String prev_page_url) {
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
             * title : 这里是标题
             * titleId : 5
             * desc :
             * updatedAt : 2021-04-07 16:52:51
             * readNum : 0
             * type : 2
             * url : http://wwdfasdfdf
             */

            private String title;
            private int titleId;
            private String desc;
            private String updatedAt;
            private int readNum;
            private int type;
            private String url;

            @Override
            public String toString() {
                return "DataBean{" +
                        "title='" + title + '\'' +
                        ", titleId=" + titleId +
                        ", desc='" + desc + '\'' +
                        ", updatedAt='" + updatedAt + '\'' +
                        ", readNum=" + readNum +
                        ", type=" + type +
                        ", url='" + url + '\'' +
                        '}';
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getTitleId() {
                return titleId;
            }

            public void setTitleId(int titleId) {
                this.titleId = titleId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public int getReadNum() {
                return readNum;
            }

            public void setReadNum(int readNum) {
                this.readNum = readNum;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
