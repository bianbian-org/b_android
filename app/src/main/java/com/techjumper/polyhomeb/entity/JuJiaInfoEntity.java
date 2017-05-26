package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class JuJiaInfoEntity extends BaseEntity<JuJiaInfoEntity.DataBean> {

    public static class DataBean {
        /**
         * page_url : /jujia/mobile/app/index
         * has_more : 1
         * servers : [{"cover_url":"/upload/files/206.jpg","server_name":"快递服务","page_url":"/jujia/mobile/user/express"},{"cover_url":"/upload/files/206.jpg","server_name":"鲜花服务","page_url":"/jujia/mobile/user/express"}]
         */

        private String page_url;
        private int has_more;
        private List<ServersBean> servers;

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public List<ServersBean> getServers() {
            return servers;
        }

        public void setServers(List<ServersBean> servers) {
            this.servers = servers;
        }

        public static class ServersBean {
            /**
             * cover_url : /upload/files/206.jpg
             * server_name : 快递服务
             * page_url : /jujia/mobile/user/express
             */

            private String cover_url;
            private String server_name;
            private String page_url;

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getServer_name() {
                return server_name;
            }

            public void setServer_name(String server_name) {
                this.server_name = server_name;
            }

            public String getPage_url() {
                return page_url;
            }

            public void setPage_url(String page_url) {
                this.page_url = page_url;
            }
        }
    }
}
