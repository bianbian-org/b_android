package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/11/22.
 */

public class AlipayEntity extends BaseEntity<AlipayEntity.AlipayDataEntity> {

    public static class AlipayDataEntity {
        private String alipay;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }
    }
}
