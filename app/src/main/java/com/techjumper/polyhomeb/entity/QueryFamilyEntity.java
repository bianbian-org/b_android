package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class QueryFamilyEntity extends BaseEntity<QueryFamilyEntity.DataEntity> {
    public static class DataEntity {
        private String id;
        private String family_name;
        private String device_id;
        private String jxj_user_name;
        private String jxj_password;
        private String lc_mobile;
        private String has_binding;
        private String Ip;
        private String Port;
        private String manager_id;

        public String getLc_mobile() {
            return lc_mobile;
        }

        public void setLc_mobile(String lc_mobile) {
            this.lc_mobile = lc_mobile;
        }

        public String getManager_id() {
            return manager_id;
        }

        public void setManager_id(String manager_id) {
            this.manager_id = manager_id;
        }

        public String getIp() {
            return Ip;
        }

        public void setIp(String ip) {
            Ip = ip;
        }

        public String getPort() {
            return Port;
        }

        public void setPort(String port) {
            Port = port;
        }

        public String getHas_binding() {
            return has_binding;
        }

        public void setHas_binding(String has_binding) {
            this.has_binding = has_binding;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getJxj_user_name() {
            return jxj_user_name;
        }

        public void setJxj_user_name(String jxj_user_name) {
            this.jxj_user_name = jxj_user_name;
        }

        public String getJxj_password() {
            return jxj_password;
        }

        public void setJxj_password(String jxj_password) {
            this.jxj_password = jxj_password;
        }
    }
}
