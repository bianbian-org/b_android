package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class TimerClickEntity {

    public static final String YIJIAN_HOME = "1001";
    public static final String YIJIAN_AD = "1002";
    public static final String YIJIAN_JUJIA = "1003";

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
