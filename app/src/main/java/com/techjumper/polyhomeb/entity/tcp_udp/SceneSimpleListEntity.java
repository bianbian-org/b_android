package com.techjumper.polyhomeb.entity.tcp_udp;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/4/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SceneSimpleListEntity extends BaseReceiveEntity<SceneSimpleListEntity.DataEntity> {

    public static class DataEntity {

        private List<SceneListEntity> senceList;

        public List<SceneListEntity> getSenceList() {
            return senceList;
        }

        public void setSenceList(List<SceneListEntity> senceList) {
            this.senceList = senceList;
        }

        public static class SceneListEntity {
            private String security;
            private String senceid;
            private String name;
            private String sencestate;
            private String type;
            private String securitydelaytime;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSecuritydelaytime() {
                return securitydelaytime;
            }

            public void setSecuritydelaytime(String securitydelaytime) {
                this.securitydelaytime = securitydelaytime;
            }

            public String getSencestate() {
                return sencestate;
            }

            public void setSencestate(String sencestate) {
                this.sencestate = sencestate;
            }

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "SceneListEntity{" +
                        "security='" + security + '\'' +
                        ", senceid='" + senceid + '\'' +
                        ", name='" + name + '\'' +
                        ", sencestate='" + sencestate + '\'' +
                        ", type='" + type + '\'' +
                        ", securitydelaytime='" + securitydelaytime + '\'' +
                        '}';
            }
        }
    }
}
