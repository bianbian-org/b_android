package com.techjumper.polyhomeb.entity;

import com.google.gson.annotations.SerializedName;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/10
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PaymentsEntity extends BaseEntity<PaymentsEntity.DataBean> {

    public static class DataBean {
        private WxpayBean wxpay;
        private AliPayBean alipay;
        private UnionBean union;

        public UnionBean getUnion() {
            return union;
        }

        public void setUnion(UnionBean union) {
            this.union = union;
        }

        public WxpayBean getWxpay() {
            return wxpay;
        }

        public void setWxpay(WxpayBean wxpay) {
            this.wxpay = wxpay;
        }

        public AliPayBean getAlipay() {
            return alipay;
        }

        public void setAlipay(AliPayBean alipay) {
            this.alipay = alipay;
        }

        public static class AliPayBean{
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

        public static class WxpayBean {
            private String appid;
            private String partnerid;
            private String prepayid;
            @SerializedName("package")
            private String packageX;
            private String noncestr;
            private String timestamp;
            private String sign;

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }

        public static class UnionBean{
            private String mode;
            private String tn;

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public String getTn() {
                return tn;
            }

            public void setTn(String tn) {
                this.tn = tn;
            }
        }
    }
}
