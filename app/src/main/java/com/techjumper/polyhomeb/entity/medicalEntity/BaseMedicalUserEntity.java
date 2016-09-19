package com.techjumper.polyhomeb.entity.medicalEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class BaseMedicalUserEntity<T> {

    private T data;
    private int status;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
