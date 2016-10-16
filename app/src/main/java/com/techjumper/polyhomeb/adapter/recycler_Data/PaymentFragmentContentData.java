package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PaymentFragmentContentData {

    private int btnName;
    private String title;
    private String content;
    private String time;
    private double price;
    private int day;
    private double expiry_price;
    private String order_num;
    private int status;
    private int is_late;
    private String payment_date;

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public int getIs_late() {
        return is_late;
    }

    public void setIs_late(int is_late) {
        this.is_late = is_late;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public double getExpiry_price() {
        return expiry_price;
    }

    public void setExpiry_price(double expiry_price) {
        this.expiry_price = expiry_price;
    }

    public int getBtnName() {
        return btnName;
    }

    public void setBtnName(int btnName) {
        this.btnName = btnName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
