package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/11/22.
 */

public class WxpayEntity extends BaseEntity<WxpayEntity.WxpayDataEntity> {

    public static class WxpayDataEntity {
        private WxpayItemEntity wxpay;

        public WxpayItemEntity getWxpay() {
            return wxpay;
        }

        public void setWxpay(WxpayItemEntity wxpay) {
            this.wxpay = wxpay;
        }
    }

    public static class WxpayItemEntity {
        private String code_url;
        private String sign;

        public String getCode_url() {
            return code_url;
        }

        public void setCode_url(String code_url) {
            this.code_url = code_url;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }


}
