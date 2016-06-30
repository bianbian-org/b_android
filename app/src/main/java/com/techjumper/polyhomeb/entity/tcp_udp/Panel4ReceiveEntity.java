package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/5/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Panel4ReceiveEntity extends BaseReceiveEntity<Panel4ReceiveEntity.DataBean> {

    public static class DataBean {
        private String security;
        private String senceid;
        private String type;
        private String sencestate;
        private String securitydelaytime;
        private String name;

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public String getSenceid() {
            return senceid;
        }

        public void setSenceid(String senceid) {
            this.senceid = senceid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSencestate() {
            return sencestate;
        }

        public void setSencestate(String sencestate) {
            this.sencestate = sencestate;
        }

        public String getSecuritydelaytime() {
            return securitydelaytime;
        }

        public void setSecuritydelaytime(String securitydelaytime) {
            this.securitydelaytime = securitydelaytime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
