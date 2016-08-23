package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JS2JavaPageJumpEntity extends BaseEntity<JS2JavaPageJumpEntity.DataBean> {

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
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
