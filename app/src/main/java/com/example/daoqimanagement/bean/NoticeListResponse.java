package com.example.daoqimanagement.bean;

import java.util.List;

public class NoticeListResponse {

    @Override
    public String toString() {
        return "NoticeListResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"current_page":1,"data":[{"noticeId":11,"createTime":"2021-03-04 15:32:24","title":"延迟审核申请","content":"财务审核已完成","isRead":1},{"noticeId":2,"createTime":"2021-02-25 17:14:27","title":"审核信息","content":"请尽快审核","isRead":1}],"first_page_url":"http://127.0.0.1:9510/v1/notice/list?page=1","from":1,"last_page":1,"last_page_url":"http://127.0.0.1:9510/v1/notice/list?page=1","next_page_url":null,"path":"http://127.0.0.1:9510/v1/notice/list","per_page":15,"prev_page_url":null,"to":2,"total":2}
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
                    ", last_page=" + last_page +
                    ", first_page_url='" + first_page_url + '\'' +
                    ", from=" + from +
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
         * data : [{"noticeId":11,"createTime":"2021-03-04 15:32:24","title":"延迟审核申请","content":"财务审核已完成","isRead":1},{"noticeId":2,"createTime":"2021-02-25 17:14:27","title":"审核信息","content":"请尽快审核","isRead":1}]
         * first_page_url : http://127.0.0.1:9510/v1/notice/list?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://127.0.0.1:9510/v1/notice/list?page=1
         * next_page_url : null
         * path : http://127.0.0.1:9510/v1/notice/list
         * per_page : 15
         * prev_page_url : null
         * to : 2
         * total : 2
         */


        private int current_page;
        private int last_page;
        private String first_page_url;
        private int from;

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

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public String getLast_page_url() {
            return last_page_url;
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
             * noticeId : 11
             * createTime : 2021-03-04 15:32:24
             * title : 延迟审核申请
             * content : 财务审核已完成
             * isRead : 1
             */

            private int noticeId;
            private String createTime;
            private String title;
            private String content;
            private int isRead;
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

            public int getNoticeId() {
                return noticeId;
            }

            public void setNoticeId(int noticeId) {
                this.noticeId = noticeId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }
        }
    }
}
