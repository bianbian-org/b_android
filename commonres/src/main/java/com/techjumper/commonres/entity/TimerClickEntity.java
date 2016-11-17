package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class TimerClickEntity {

    public static final String YIJIAN_HOME = "1001";
    public static final String YIJIAN_AD = "1002";
    public static final String YIJIAN_JUJIA = "1003";
    public static final String ONCLICK_JUJIA = "1004";
    public static final String ONCLICK_SMARTHOME = "1005";
    public static final String ONCLICK_PROPERTY = "1006";
    public static final String ONCLICK_INFO = "1007";
    public static final String ONCLICK_SHOPPING = "1008";
    public static final String ONCLICK_MEDICAL = "1009";
    public static final String ONCLICK_TALK = "1010";
    public static final String ONCLICK_SETTING = "1011";
    public static final String ONCLICK_VIDEO = "1012";
    public static final String ONCLICK_ONE_AD = "1013";
    public static final String ONCLICK_TWO_AD = "1014";
    public static final String STAY_JUJIA = "1015";
    public static final String STAY_PROPERTY = "1016";
    public static final String STAY_INFO = "1017";
    public static final String STAY_SHOPPING = "1018";
    public static final String STAY_SMARTHOME = "1019";
    public static final String STAY_MEDICAL = "1020";
    public static final String STAY_TALK = "1021";
    public static final String STAY_SETTING = "1022";
    public static final String STAY_VIDEO = "1023";
    public static final String ONCLICK_CALL_SERVICE = "1024";

    private List<TimerClickItemEntity> datas;

    public List<TimerClickItemEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<TimerClickItemEntity> datas) {
        this.datas = datas;
    }

    public static class TimerClickItemEntity {
        private String event_id;
        private String start_time;
        private String end_time;

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
