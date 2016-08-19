package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SectionsEntity extends BaseEntity<SectionsEntity.DataBean> {

    //{"error_code":0,"error_msg":null,"data":{"result":[[1,"跳蚤"],[2,"亲子"],[3,"闲谈"],[4,"活动"]]}}

    public static class DataBean {

        private String[][] result;

        public String[][] getResult() {
            return result;
        }
    }
}
