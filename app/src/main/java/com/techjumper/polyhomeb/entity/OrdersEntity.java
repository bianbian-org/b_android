package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class OrdersEntity extends BaseEntity<OrdersEntity.DataBean> {

    public static class DataBean {
        private double total_price;  //#缴费总金额 238.43

        private List<OrdersBean> orders;

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean {
            private String order_number;  // #订单号   "2014731502779997"
            private int pay_type;   //#缴费类型 1-物业费 2-水费 3-电费 4-燃气费 5-其他
            private String pay_name;  // #费用名称   "8月份电费"
            private String expiry_date;   //#缴费日期  "2016-09-22"
            private int status;   //#缴费状态 1-未缴费 2-已缴费
            private String object;   //#缴费对象    "1栋3单元21-111"
            private double price;   //#缴费金额    96.68
            private int is_late;    // #是否逾期   0-没逾期, 1-逾期
            private double expiry_price;  //#滞纳金   15.19
            private int expiry;    //#逾期时间（天）  24

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public int getPay_type() {
                return pay_type;
            }

            public void setPay_type(int pay_type) {
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getIs_late() {
                return is_late;
            }

            public void setIs_late(int is_late) {
                this.is_late = is_late;
            }

            public double getExpiry_price() {
                return expiry_price;
            }

            public void setExpiry_price(double expiry_price) {
                this.expiry_price = expiry_price;
            }

            public int getExpiry() {
                return expiry;
            }

            public void setExpiry(int expiry) {
                this.expiry = expiry;
            }
        }
    }
}
