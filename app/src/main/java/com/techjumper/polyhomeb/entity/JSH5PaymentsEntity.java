package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JSH5PaymentsEntity {

    private int type = -1;
    private String back_type;
    private AlipayBean alipay;

    public String getBack_type() {
        return back_type;
    }

    public void setBack_type(String back_type) {
        this.back_type = back_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AlipayBean getAlipay() {
        return alipay;
    }

    public void setAlipay(AlipayBean alipay) {
        this.alipay = alipay;
    }

    public static class AlipayBean {
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
