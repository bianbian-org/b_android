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
