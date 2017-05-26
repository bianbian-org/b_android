package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CheckInEntity extends BaseEntity<CheckInEntity.DataBean> {

    public static class DataBean {

        //这两个字段,点击签到的时候会返回
        private String result;
        private List<Integer> sign_days;

        //查询签到数据的时候,只会返回sign_days

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<Integer> getSign_days() {
            return sign_days;
        }

        public void setSign_days(List<Integer> sign_days) {
            this.sign_days = sign_days;
        }
    }
}
