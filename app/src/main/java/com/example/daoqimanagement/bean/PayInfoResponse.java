package com.example.daoqimanagement.bean;

public class PayInfoResponse {

    @Override
    public String toString() {
        return "PayInfoResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * code : 0
     * data : {"businessName":"杭州道奇医疗器械有限公司","bank":"招商银行","bankNo":"99829123131233","amount":"100000","bigVoucher":"壹拾万元整"}
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
                    "businessName='" + businessName + '\'' +
                    ", bank='" + bank + '\'' +
                    ", bankNo='" + bankNo + '\'' +
                    ", amount='" + amount + '\'' +
                    ", bigVoucher='" + bigVoucher + '\'' +
                    '}';
        }

        /**
         * businessName : 杭州道奇医疗器械有限公司
         * bank : 招商银行
         * bankNo : 99829123131233
         * amount : 100000
         * bigVoucher : 壹拾万元整
         */


        private String businessName;
        private String bank;
        private String bankNo;
        private String amount;
        private String bigVoucher;

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBigVoucher() {
            return bigVoucher;
        }

        public void setBigVoucher(String bigVoucher) {
            this.bigVoucher = bigVoucher;
        }
    }
}
