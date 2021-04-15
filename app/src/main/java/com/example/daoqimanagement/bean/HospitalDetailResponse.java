package com.example.daoqimanagement.bean;

import java.util.List;

public class HospitalDetailResponse {


    /**
     * code : 0
     * data : {"areaName":"西藏公立医院","level":"三级特等","nature":"中立","hospitalName":"是我医院","detail":"是我的详情","headPic":"/upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg","cooperate":[{"productName":"八戒睡眠-设备投放","truename":"荔枝面9","status":1}],"contact":[{"name":"张三","mobile":"11012011911","department":"呼吸科","position":"副主任"}]}
     * msg : ok
     */

    private int code;
    private DataBean data;
    private String msg;

    @Override
    public String toString() {
        return "HospitalDetailResponse{" +
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
                    "areaName='" + areaName + '\'' +
                    ", level='" + level + '\'' +
                    ", nature='" + nature + '\'' +
                    ", hospitalName='" + hospitalName + '\'' +
                    ", detail='" + detail + '\'' +
                    ", headPic='" + headPic + '\'' +
                    ", cooperate=" + cooperate +
                    ", contact=" + contact +
                    '}';
        }

        /**
         * areaName : 西藏公立医院
         * level : 三级特等
         * nature : 中立
         * hospitalName : 是我医院
         * detail : 是我的详情
         * headPic : /upload/20210224/fc640e8f7f2cf3155e5415c5f83a3a8d.jpg
         * cooperate : [{"productName":"八戒睡眠-设备投放","truename":"荔枝面9","status":1}]
         * contact : [{"name":"张三","mobile":"11012011911","department":"呼吸科","position":"副主任"}]
         */


        private String areaName;
        private String level;
        private String nature;
        private String hospitalName;
        private String detail;
        private String headPic;
        private List<CooperateBean> cooperate;
        private List<ContactBean> contact;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
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

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public List<CooperateBean> getCooperate() {
            return cooperate;
        }

        public void setCooperate(List<CooperateBean> cooperate) {
            this.cooperate = cooperate;
        }

        public List<ContactBean> getContact() {
            return contact;
        }

        public void setContact(List<ContactBean> contact) {
            this.contact = contact;
        }

        public static class CooperateBean {
            @Override
            public String toString() {
                return "CooperateBean{" +
                        "productName='" + productName + '\'' +
                        ", truename='" + truename + '\'' +
                        ", status=" + status +
                        '}';
            }

            /**
             * productName : 八戒睡眠-设备投放
             * truename : 荔枝面9
             * status : 1
             */


            private String productName;
            private String truename;
            private int status;

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getTruename() {
                return truename;
            }

            public void setTruename(String truename) {
                this.truename = truename;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class ContactBean {
            @Override
            public String toString() {
                return "ContactBean{" +
                        "name='" + name + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", department='" + department + '\'' +
                        ", position='" + position + '\'' +
                        '}';
            }

            /**
             * name : 张三
             * mobile : 11012011911
             * department : 呼吸科
             * position : 副主任
             */


            private String name;
            private String mobile;
            private String department;
            private String position;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getDepartment() {
                return department;
            }

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }
        }
    }
}
