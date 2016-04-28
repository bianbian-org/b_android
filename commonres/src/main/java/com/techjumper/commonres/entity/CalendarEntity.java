package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/4/28.
 */
public class CalendarEntity extends BaseEntity<CalendarEntity.CalendarDataEntity> {

    public static class CalendarDataEntity {
        private String date;
        //农历年
        private String lunarYear;
        //生肖
        private String animalsYear;
        //农历日期
        private String lunar;
        //宜
        private String suit;
        //忌
        private String avoid;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLunarYear() {
            return lunarYear;
        }

        public void setLunarYear(String lunarYear) {
            this.lunarYear = lunarYear;
        }

        public String getAnimalsYear() {
            return animalsYear;
        }

        public void setAnimalsYear(String animalsYear) {
            this.animalsYear = animalsYear;
        }

        public String getLunar() {
            return lunar;
        }

        public void setLunar(String lunar) {
            this.lunar = lunar;
        }

        public String getSuit() {
            return suit;
        }

        public void setSuit(String suit) {
            this.suit = suit;
        }

        public String getAvoid() {
            return avoid;
        }

        public void setAvoid(String avoid) {
            this.avoid = avoid;
        }

        @Override
        public String toString() {
            return "DateDataEntity{" +
                    "date='" + date + '\'' +
                    ", lunarYear='" + lunarYear + '\'' +
                    ", animalsYear='" + animalsYear + '\'' +
                    ", lunar='" + lunar + '\'' +
                    ", suit='" + suit + '\'' +
                    ", avoid='" + avoid + '\'' +
                    '}';
        }
    }
}
