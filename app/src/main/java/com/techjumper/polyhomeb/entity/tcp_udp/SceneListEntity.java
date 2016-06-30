package com.techjumper.polyhomeb.entity.tcp_udp;

import java.io.Serializable;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 6/3/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SceneListEntity extends BaseReceiveEntity<SceneListEntity.DataBean> {

    public static final String SCENE_SECURITY = "secu";
    public static final String SCENE_NORMAL = "no";
    public static final String SCENE_NOSECU = "nosecu";

    public static final String TYPE_COMMONSCENE = "commonscene"; //普通情景
    public static final String TYPE_TIMERIFFIC = "timeriffic"; //定时情景
    public static final String TYPE_SECURITYSCENE = "securityscene"; //安防情景

    public static class DataBean {
        /**
         * device : [{"state":"1","type":"socket","sn":"polyhome_21,111","delaytime":"3000"},{"state":"1","way":"1","sn":"polyhome_44,5","delaytime":"3000","type":"light"}]
         * trigger : [{"type":"periodicaltrigger","time":"4:15:45:00","type1":"1"},{"type":"periodicaltrigger","time":"15:45:00","type1":"0"},{"type":"periodicaltrigger","time":"2016:4:1:15:45:00","type1":"2"}]
         * security : secu
         * senceid : 1459402010074
         * name : SENCE
         */

        private List<SenceListBean> senceList;

        public List<SenceListBean> getSenceList() {
            return senceList;
        }

        public void setSenceList(List<SenceListBean> senceList) {
            this.senceList = senceList;
        }

        public static class SenceListBean {
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

            /**
             * state : 1
             * type : socket
             * sn : polyhome_21,111
             * delaytime : 3000
             */

            private List<DeviceBean> device;
            /**
             * type : periodicaltrigger
             * time : 4:15:45:00
             * type1 : 1
             */

            private List<TriggerBean> trigger;

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

            public List<DeviceBean> getDevice() {
                return device;
            }

            public void setDevice(List<DeviceBean> device) {
                this.device = device;
            }

            public List<TriggerBean> getTrigger() {
                return trigger;
            }

            public void setTrigger(List<TriggerBean> trigger) {
                this.trigger = trigger;
            }

            public static class DeviceBean implements Serializable {
                private String state;
                private String type;
                private String sn;
                private String delaytime;

                private String way;

                private String key;
                private String luminance;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getLuminance() {
                    return luminance;
                }

                public void setLuminance(String luminance) {
                    this.luminance = luminance;
                }

                public String getWay() {
                    return way;
                }

                public void setWay(String way) {
                    this.way = way;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getSn() {
                    return sn;
                }

                public void setSn(String sn) {
                    this.sn = sn;
                }

                public String getDelaytime() {
                    return delaytime;
                }

                public void setDelaytime(String delaytime) {
                    this.delaytime = delaytime;
                }
            }

            public static class TriggerBean implements Serializable {
                private String type;
                private String time;
                private String type1;
                private String way;

                private String sn;
                private String num;
                private String state;
                private String condition;
                private String paramtype;
                private String value;

                public String getCondition() {
                    return condition;
                }

                public void setCondition(String condition) {
                    this.condition = condition;
                }

                public String getParamtype() {
                    return paramtype;
                }

                public void setParamtype(String paramtype) {
                    this.paramtype = paramtype;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getSn() {
                    return sn;
                }

                public void setSn(String sn) {
                    this.sn = sn;
                }

                public String getWay() {
                    return way;
                }

                public void setWay(String way) {
                    this.way = way;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getType1() {
                    return type1;
                }

                public void setType1(String type1) {
                    this.type1 = type1;
                }
            }
        }
    }
}
