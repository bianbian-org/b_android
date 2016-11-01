package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/26
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UserFamiliesAndVillagesEntity extends BaseEntity<UserFamiliesAndVillagesEntity.DataBean> {

    public static class DataBean {

        private List<FamilyInfosBean> family_infos;
        private List<VillageInfosBean> village_infos;

        public List<FamilyInfosBean> getFamily_infos() {
            return family_infos;
        }

        public void setFamily_infos(List<FamilyInfosBean> family_infos) {
            this.family_infos = family_infos;
        }

        public List<VillageInfosBean> getVillage_infos() {
            return village_infos;
        }

        public void setVillage_infos(List<VillageInfosBean> village_infos) {
            this.village_infos = village_infos;
        }

        public static class FamilyInfosBean {
            private int id;
            private String family_name;
            private int village_id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFamily_name() {
                return family_name;
            }

            public void setFamily_name(String family_name) {
                this.family_name = family_name;
            }

            public int getVillage_id() {
                return village_id;
            }

            public void setVillage_id(int village_id) {
                this.village_id = village_id;
            }
        }

        public static class VillageInfosBean {
            private int village_id;
            private String village_name;
            /**
             * room_num : 1-1-102
             * verified : 0
             */

            private List<RoomsBean> rooms;

            public int getVillage_id() {
                return village_id;
            }

            public void setVillage_id(int village_id) {
                this.village_id = village_id;
            }

            public String getVillage_name() {
                return village_name;
            }

            public void setVillage_name(String village_name) {
                this.village_name = village_name;
            }

            public List<RoomsBean> getRooms() {
                return rooms;
            }

            public void setRooms(List<RoomsBean> rooms) {
                this.rooms = rooms;
            }

            public static class RoomsBean {
                private String room_num;
                private int verified;

                public String getRoom_num() {
                    return room_num;
                }

                public void setRoom_num(String room_num) {
                    this.room_num = room_num;
                }

                public int getVerified() {
                    return verified;
                }

                public void setVerified(int verified) {
                    this.verified = verified;
                }

                @Override
                public String toString() {
                    return "RoomsBean{" +
                            "room_num='" + room_num + '\'' +
                            ", verified=" + verified +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "VillageInfosBean{" +
                        "village_id=" + village_id +
                        ", village_name='" + village_name + '\'' +
                        ", rooms=" + rooms +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "family_infos=" + family_infos +
                    ", village_infos=" + village_infos +
                    '}';
        }
    }
}
