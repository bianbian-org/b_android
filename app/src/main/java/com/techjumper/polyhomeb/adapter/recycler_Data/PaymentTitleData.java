package com.techjumper.polyhomeb.adapter.recycler_Data;

import com.techjumper.polyhomeb.entity.PaymentTypeEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/7
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PaymentTitleData {

    private String title;
    private double total;
    private int where;
    private List<PaymentTypeEntity.DataBean.ItemsBean> items;
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<PaymentTypeEntity.DataBean.ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<PaymentTypeEntity.DataBean.ItemsBean> items) {
        this.items = items;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
