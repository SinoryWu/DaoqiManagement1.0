package com.example.daoqimanagement.bean;

import java.util.List;

public class ApprovalListResponse {


    /**
     * code : 0
     * data : {"current_page":1,"data":[{"approvalId":119,"createdAt":"2021-03-25 15:48:35","title":"延期保证金待审核","truename":"荔枝面","reason":"我明年一定能开发成功","status":1}],"first_page_url":"http://127.0.0.1:9510/v1/approval/list?page=1","from":1,"last_page":1,"last_page_url":"http://127.0.0.1:9510/v1/approval/list?page=1","next_page_url":null,"path":"http://127.0.0.1:9510/v1/approval/list","per_page":15,"prev_page_url":null,"to":7,"total":7}
     * msg : ok
     */

    private int code;
    private DataBeanX data;
    private String msg;

    @Override
    public String toString() {
        return "ApprovalListResponse{" +
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
        /**
         * current_page : 1
         * data : [{"approvalId":119,"createdAt":"2021-03-25 15:48:35","title":"延期保证金待审核","truename":"荔枝面","reason":"我明年一定能开发成功","status":1}]
         * first_page_url : http://127.0.0.1:9510/v1/approval/list?page=1
         * from : 1
         * last_page : 1
         * last_page_url : http://127.0.0.1:9510/v1/approval/list?page=1
         * next_page_url : null
         * path : http://127.0.0.1:9510/v1/approval/list
         * per_page : 15
         * prev_page_url : null
         * to : 7
         * total : 7
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
             * approvalId : 119
             * createdAt : 2021-03-25 15:48:35
             * title : 延期保证金待审核
             * truename : 荔枝面
             * reason : 我明年一定能开发成功
             * status : 1
             */

            private int approvalId;
            private String createdAt;
            private String title;
            private String truename;
            private String reason;
            private int status;
            private int current_page;
            private int last_page;

            @Override
            public String toString() {
                return "DataBean{" +
                        "approvalId=" + approvalId +
                        ", createdAt='" + createdAt + '\'' +
                        ", title='" + title + '\'' +
                        ", truename='" + truename + '\'' +
                        ", reason='" + reason + '\'' +
                        ", status=" + status +
                        ", current_page=" + current_page +
                        ", last_page=" + last_page +
                        '}';
            }

            public int getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(int current_page) {
                this.current_page = current_page;
            }

            public int getLast_page() {
                return last_page;
            }

            public void setLast_page(int last_page) {
                this.last_page = last_page;
            }

            public int getApprovalId() {
                return approvalId;
            }

            public void setApprovalId(int approvalId) {
                this.approvalId = approvalId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTruename() {
                return truename;
            }

            public void setTruename(String truename) {
                this.truename = truename;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
