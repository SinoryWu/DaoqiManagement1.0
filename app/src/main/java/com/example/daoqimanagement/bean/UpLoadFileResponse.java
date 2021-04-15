package com.example.daoqimanagement.bean;

public class UpLoadFileResponse {


    @Override
    public String toString() {
        return "UpLoadFileResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"fileId":359,"filename":"32BEC946-2DA8-4572-8DD8-A2E28D40AAF6.jpeg"}
     * msg : ok
     */


    private int code;
    private DataBean data;
    private String msg;

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
                    "fileId=" + fileId +
                    ", filename='" + filename + '\'' +
                    '}';
        }

        /**
         * fileId : 359
         * filename : 32BEC946-2DA8-4572-8DD8-A2E28D40AAF6.jpeg
         */



        private int fileId;
        private String filename;

        public int getFileId() {
            return fileId;
        }

        public void setFileId(int fileId) {
            this.fileId = fileId;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }


}
