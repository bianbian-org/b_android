package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TrueEntity extends BaseEntity<TrueEntity.TrueDataEntity> {

    public static class TrueDataEntity {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "TrueDataEntity{" +
                    "result='" + result + '\'' +
                    '}';
        }
    }
}
