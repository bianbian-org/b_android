package com.techjumper.polyhomeb.entity.event;

import com.techjumper.polyhomeb.entity.PayEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class H5PayEvent {

    private int hashCode;
    private PayEntity payEntity;

    public H5PayEvent(int hashCode, PayEntity payEntity) {
        this.hashCode = hashCode;
        this.payEntity = payEntity;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public PayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(PayEntity payEntity) {
        this.payEntity = payEntity;
    }
}
