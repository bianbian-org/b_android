package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class UpdateInfoEntity extends BaseEntity<UpdateInfoEntity.DataBean> {
    public static class DataBean {
        /**
         * version : 1
         * url : /upload/files/main-host.apk
         * package_name : com.andriod
         */

        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            private String version;
            private String url;
            private String package_name;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPackage_name() {
                return package_name;
            }

            public void setPackage_name(String package_name) {
                this.package_name = package_name;
            }
        }
    }
}
