package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/10
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class PaymentsEntity extends BaseEntity<PaymentsEntity.DataBean> {

    public static class DataBean {
        private String parms_str;
        private String sign;

        public String getParms_str() {
            return parms_str;
        }

        public void setParms_str(String parms_str) {
            this.parms_str = parms_str;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
