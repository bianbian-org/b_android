package com.techjumper.polyhomeb.adapter.recycler_Data;

import com.techjumper.polyhomeb.entity.medicalEntity.MedicalMainEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainData {

    private String data;
    private int position;
    private MedicalMainEntity.DataBean dataBean;

    public MedicalMainEntity.DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(MedicalMainEntity.DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
