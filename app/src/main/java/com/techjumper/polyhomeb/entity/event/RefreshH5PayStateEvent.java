package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class RefreshH5PayStateEvent {

    private String order_number;

    public RefreshH5PayStateEvent(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_number() {

        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
