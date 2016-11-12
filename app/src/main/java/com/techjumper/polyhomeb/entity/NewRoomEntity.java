package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class NewRoomEntity extends BaseEntity<NewRoomEntity.DataEntity> {

    public static class DataEntity {
        private int room_id;
        private String room_name;
        private String family_id;

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public void setFamily_id(String family_id) {
            this.family_id = family_id;
        }

        public int getRoom_id() {
            return room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public String getFamily_id() {
            return family_id;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "room_id=" + room_id +
                    ", room_name='" + room_name + '\'' +
                    ", family_id='" + family_id + '\'' +
                    '}';
        }
    }
}
