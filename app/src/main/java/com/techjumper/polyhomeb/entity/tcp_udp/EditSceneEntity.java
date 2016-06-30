package com.techjumper.polyhomeb.entity.tcp_udp;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/4/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class EditSceneEntity {

    private ParamBean param;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        private String name;
        private String security;
        private String senceid;

        public String getSenceid() {
            return senceid;
        }

        public void setSenceid(String senceid) {
            this.senceid = senceid;
        }

        private List<TriggerBean> trigger;

        private List<DeviceBean> device;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public List<TriggerBean> getTrigger() {
            return trigger;
        }

        public void setTrigger(List<TriggerBean> trigger) {
            this.trigger = trigger;
        }

        public List<DeviceBean> getDevice() {
            return device;
        }

        public void setDevice(List<DeviceBean> device) {
            this.device = device;
        }

        public static class TriggerBean {
            private String type;       //类型
            private String type1;      //周期类型   "0"代表每天 "1"代表每周  "2"单次执行  0: 11:05:00  1: 1:11:05:00(注 1为周日2为周一以此类推)  2：2016:4:1:11:05:00
            private String way;        //面板的第几个按键
            private String sn;         //sn
            private String time;       //具体时间 注:type1等于1时：周日为 1 周一为 2 ... 周六为 7
            private String num;        //代表开锁人的编号
            private String state;      //门锁状态
            private String condition;  //触发条件 "0"大于 "1"小于 "2"区间
            private String paramtype;  //具体的触发类型 "0"代表PM2.5  "1"代表CO2  "2"代表温度  "3"代表湿度
            private String value;      //condition == 0: 10:15 大于15  condition == 1: 10:15 小于10 condition == 2: 10:15 大于等于10 小于等于15

            public String getType1() {
                return type1;
            }

            public void setType1(String type1) {
                this.type1 = type1;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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

            public String getClicksenceid() {
                return clicksenceid;
            }

            public void setClicksenceid(String clicksenceid) {
                this.clicksenceid = clicksenceid;
            }

            private String clicksenceid;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getWay() {
                return way;
            }

            public void setWay(String way) {
                this.way = way;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            @Override
            public String toString() {
                return "TriggerBean{" +
                        "type='" + type + '\'' +
                        ", type1='" + type1 + '\'' +
                        ", way='" + way + '\'' +
                        ", sn='" + sn + '\'' +
                        ", time='" + time + '\'' +
                        ", num='" + num + '\'' +
                        ", state='" + state + '\'' +
                        ", condition='" + condition + '\'' +
                        ", paramtype='" + paramtype + '\'' +
                        ", value='" + value + '\'' +
                        ", clicksenceid='" + clicksenceid + '\'' +
                        '}';
            }
        }

        public static class DeviceBean {
            private String type;         //类型
            private String delaytime;    //延迟启动的时间
            private String sn;           //sn
            private String state;        //设备
            private String luminance;    //亮度
            private String way;

            public String getWay() {
                return way;
            }

            public void setWay(String way) {
                this.way = way;
            }

            public String getLuminance() {
                return luminance;
            }

            public void setLuminance(String luminance) {
                this.luminance = luminance;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDelaytime() {
                return delaytime;
            }

            public void setDelaytime(String delaytime) {
                this.delaytime = delaytime;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            @Override
            public String toString() {
                return "DeviceBean{" +
                        "type='" + type + '\'' +
                        ", delaytime='" + delaytime + '\'' +
                        ", sn='" + sn + '\'' +
                        ", state='" + state + '\'' +
                        ", luminance='" + luminance + '\'' +
                        ", way='" + way + '\'' +
                        '}';
            }
        }
    }
}
