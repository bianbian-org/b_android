package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JS2JavaImageViewEntity extends BaseEntity<JS2JavaImageViewEntity.DataBean> {

    public static class DataBean {
        private String method;
        private List<Params> params;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public List<Params> getParams() {
            return params;
        }

        public void setParams(List<Params> params) {
            this.params = params;
        }

        public static class Params {
            private int index;
            private String[] images;

            public String[] getImages() {
                return images;
            }

            public void setImages(String[] images) {
                this.images = images;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }
    }
}
