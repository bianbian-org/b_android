package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JSCallPhoneNumberEvent {

    private String tel;
    private String shop_id;
    private String shop_service_id;

    public String getTel() {
        return tel;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getShop_service_id() {
        return shop_service_id;
    }

    public JSCallPhoneNumberEvent(String tel, String shop_id, String shop_service_id) {
        this.tel = tel;
        this.shop_id = shop_id;
        this.shop_service_id = shop_service_id;
    }
}
