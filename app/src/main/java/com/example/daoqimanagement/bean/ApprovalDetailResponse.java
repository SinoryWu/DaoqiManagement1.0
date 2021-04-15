package com.example.daoqimanagement.bean;

public class ApprovalDetailResponse {


    /**
     * code : 0
     * data : {"relation_id":31,"opinion":"","type":2,"userType":3,"approvalId":130,"status":1,"createdAt":"2021-03-29 13:13:19","title":"延期申请","truename":"荔枝面","prepareid":17,"hospitalName":"杭州市第一人民医院","reason":"我明年一定能开发成功","department":"停尸房","path":"/upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg","protectTime":12}
     * msg : ok
     */

    private int code;
    private DataBean data;
    private String msg;

    @Override
    public String
    toString() {
        return "ApprovalDetailResponse{" +
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
                    "relation_id=" + relation_id +
                    ", opinion='" + opinion + '\'' +
                    ", type=" + type +
                    ", userType=" + userType +
                    ", approvalId=" + approvalId +
                    ", status=" + status +
                    ", createdAt='" + createdAt + '\'' +
                    ", title='" + title + '\'' +
                    ", truename='" + truename + '\'' +
                    ", prepareid=" + prepareid +
                    ", hospitalName='" + hospitalName + '\'' +
                    ", reason='" + reason + '\'' +
                    ", department='" + department + '\'' +
                    ", path='" + path + '\'' +
                    ", protectTime=" + protectTime +
                    '}';
        }

        /**
         * relation_id : 31
         * opinion :
         * type : 2
         * userType : 3
         * approvalId : 130
         * status : 1
         * createdAt : 2021-03-29 13:13:19
         * title : 延期申请
         * truename : 荔枝面
         * prepareid : 17
         * hospitalName : 杭州市第一人民医院
         * reason : 我明年一定能开发成功
         * department : 停尸房
         * path : /upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg
         * protectTime : 12
         */


        private int relation_id;
        private String opinion;
        private int type;
        private int userType;
        private int approvalId;
        private int status;
        private String createdAt;
        private String title;
        private String truename;
        private int prepareid;
        private String hospitalName;
        private String reason;
        private String department;
        private String path;
        private int protectTime;

        public int getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(int relation_id) {
            this.relation_id = relation_id;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getApprovalId() {
            return approvalId;
        }

        public void setApprovalId(int approvalId) {
            this.approvalId = approvalId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public int getPrepareid() {
            return prepareid;
        }

        public void setPrepareid(int prepareid) {
            this.prepareid = prepareid;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getProtectTime() {
            return protectTime;
        }

        public void setProtectTime(int protectTime) {
            this.protectTime = protectTime;
        }
    }
}
