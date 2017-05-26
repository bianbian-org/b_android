package com.techjumper.polyhomeb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class C_AllRoomEntity extends BaseEntity<C_AllRoomEntity.DataEntity> {

    public static class DataEntity {

        private List<ResultEntity> result;

        public List<ResultEntity> getResult() {
            return result;
        }

        public void setResult(List<ResultEntity> result) {
            this.result = result;
        }

        public static class ResultEntity implements Parcelable {

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.room_id);
                dest.writeString(this.room_name);
            }

            public ResultEntity() {
            }

            protected ResultEntity(Parcel in) {
                this.room_id = in.readString();
                this.room_name = in.readString();
            }

            public static final Parcelable.Creator<ResultEntity> CREATOR = new Parcelable.Creator<ResultEntity>() {
                @Override
                public ResultEntity createFromParcel(Parcel source) {
                    return new ResultEntity(source);
                }

                @Override
                public ResultEntity[] newArray(int size) {
                    return new ResultEntity[size];
                }
            };
        }

    }
}
