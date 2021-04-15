package com.example.daoqimanagement.bean;

import java.util.List;

public class PrepareContactListResponse {
    private List<DataBean> contact;

    public List<DataBean> getContact() {
        return contact;
    }

    public void setContact(List<DataBean> contact) {
        this.contact = contact;
    }
    public static class DataBean{

        @Override
        public String toString() {
            return "DataBean{" +
                    "name='" + name + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", department='" + department + '\'' +
                    ", position='" + position + '\'' +
                    '}';
        }

        /**
         * name : li
         * mobile : 110
         * department : 妇产
         * position : 部长
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
