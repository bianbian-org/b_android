package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class BaseTcpArgumentsEntity {

    public static final String FILED_SIGN = "sign";
    public static final String FILED_JSON = "json";

    private String sign;

    private String json;

    public BaseTcpArgumentsEntity(String sign, String json) {
        this.sign = sign;
        this.json = json;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}

