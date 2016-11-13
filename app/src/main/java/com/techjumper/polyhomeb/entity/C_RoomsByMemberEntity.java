package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class C_RoomsByMemberEntity extends BaseEntity<C_RoomsByMemberEntity.DataEntity> {

    public static class DataEntity {

        private List<ResultEntity> result;

        public List<ResultEntity> getResult() {
            return result;
        }

        public void setResult(List<ResultEntity> result) {
            this.result = result;
        }

        public static class ResultEntity {

            private String room_id;
            private String room_name;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

        }
    }
}
