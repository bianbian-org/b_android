package com.techjumper.commonres.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 16/11/30.
 */

public class PayEntity extends BaseEntity<PayEntity.PayDataEntity> {
    public static final int NO = 1; //未缴费
    public static final int YES = 2;//已缴费
    public static final int OVER_NO = 0; //未逾期
    public static final int OVER_YES = 1;//已逾期

    public static class PayDataEntity {
        private List<PayItemEntity> orders;
        private String total_price;

        public List<PayItemEntity> getOrders() {
            return orders;
        }

        public void setOrders(List<PayItemEntity> orders) {
            this.orders = orders;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }
    }

    public static class PayItemEntity implements Serializable {
        private String order_number;
        private String pay_type;
        private String pay_name;
        private String expiry_date;
        private int status; //缴费状态 1-未缴费 2-已缴费
        private String object;
        private String price;
        private int is_late;   //是否逾期 0-未逾期 1-已逾期
        private String expiry_price;
        private int expiry;
        private String payment_date;

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_name() {
            return pay_name;
        }

        public void setPay_name(String pay_name) {
            this.pay_name = pay_name;
        }

        public String getExpiry_date() {
            return expiry_date;
        }

        public void setExpiry_date(String expiry_date) {
            this.expiry_date = expiry_date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getIs_late() {
            return is_late;
        }

        public void setIs_late(int is_late) {
            this.is_late = is_late;
        }

        public String getExpiry_price() {
            return expiry_price;
        }

        public void setExpiry_price(String expiry_price) {
            this.expiry_price = expiry_price;
        }

        public int getExpiry() {
            return expiry;
        }

        public void setExpiry(int expiry) {
            this.expiry = expiry;
        }

        public String getPayment_date() {
            return payment_date;
        }

        public void setPayment_date(String payment_date) {
            this.payment_date = payment_date;
        }
    }
}
