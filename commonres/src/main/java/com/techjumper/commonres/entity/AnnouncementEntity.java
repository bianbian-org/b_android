package com.techjumper.commonres.entity;

import java.util.List;

/**
 * Created by kevin on 16/5/17.
 */
public class AnnouncementEntity extends BaseEntity<AnnouncementEntity.AnnouncementListEntity> {

    public static final int ANNOUNCEMENT = 1; //公告
    public static final int INFORMATION = 2;  //资讯

    public static class AnnouncementListEntity {

        List<AnnouncementDataEntity> notices;

        public List<AnnouncementDataEntity> getNotices() {
            return notices;
        }

        public void setNotices(List<AnnouncementDataEntity> notices) {
            this.notices = notices;
        }
    }

    public static class AnnouncementDataEntity {
        private long id;
        private String title;
        private int types;
        private String content;
        private String time;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTypes() {
            return types;
        }

        public void setTypes(int types) {
            this.types = types;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
