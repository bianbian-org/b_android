package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RenameRoomEntity extends BaseEntity<RenameRoomEntity.DataEntity> {

    public static class DataEntity {
        private String id;  //房间ID
        private String room_name;
        private String family_id;
        private String created_at;
        private String updated_at;

        @Override
        public String toString() {
            return "DataEntity{" +
                    "id=" + id +
                    ", room_name='" + room_name + '\'' +
                    ", family_id='" + family_id + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    '}';
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getId() {
            return id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public String getFamily_id() {
            return family_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
