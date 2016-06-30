package com.techjumper.polyhomeb.entity.tcp_udp;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DeviceListEntity extends BaseReceiveEntity<DeviceListEntity.DataEntity> implements Serializable {

    public static final String DEVICE_SN = "device_sn";
    public static final String DEVICE_WAY = "device_way";
    public static final String DEVICE_STATE_CLOSE = "0";
    public static final String DEVICE_STATE_OPEN = "1";
    public static final String DEVICE_STATE_STOP = "2";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_PRODUCT_NAME = "device_product_name";
    public static final String DEVICE_PANEL_LISTS = "device_panel_lists";
    public static final String DEVICE_CHN_INDEX = "chn_index";
    public static final String ISCAMERA = "isCamera";

    public static class DataEntity implements Serializable {

        private List<ListEntity> list;

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity implements Serializable {
            private String type;
            private String room;
            private String productname;
            private String sn;
            private String way;
            private String eleState;
            private String onlinestate;
            private String name;
            private String state;
            private String value;
            private String paramtype;
            private String condition;
            private String clicksenceid;
            private String num;
            private String type1;
            private String time;

            private String cameraId;
            private boolean isCamera;

            private String key;
            private String luminance;
            private String delaytime;

            private String nightlampdelaytime;
            private String brightness;

            public String getCameraId() {
                return cameraId;
            }

            public void setCameraId(String cameraId) {
                this.cameraId = cameraId;
            }

            public boolean isCamera() {
                return isCamera;
            }

            public void setCamera(boolean camera) {
                isCamera = camera;
            }

            public String getBrightness() {
                return brightness;
            }

            public void setBrightness(String brightness) {
                this.brightness = brightness;
            }

            public String getNightlampdelaytime() {
                return nightlampdelaytime;
            }

            public void setNightlampdelaytime(String nightlampdelaytime) {
                this.nightlampdelaytime = nightlampdelaytime;
            }

            @Override
            public int hashCode() {
                int hasCode = "".hashCode();
                if (!TextUtils.isEmpty(sn)) {
                    hasCode += sn.hashCode();
                }
                if (!TextUtils.isEmpty(way)) {
                    hasCode += way.hashCode();
                }
                return hasCode;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || !(o instanceof ListEntity))
                    return false;
                ListEntity inEntity = (ListEntity) o;
                if (TextUtils.isEmpty(sn) && TextUtils.isEmpty(way)) {
                    return false;
                }
                if (TextUtils.isEmpty(sn)) {
                    return false;
                }

                if (!sn.equalsIgnoreCase(inEntity.getSn()))
                    return false;

                if (TextUtils.isEmpty(way)) {
                    return TextUtils.isEmpty(inEntity.way);
                }

                return way.equalsIgnoreCase(inEntity.getWay());

            }

            @Override
            public String toString() {
                return "ListEntity{" +
                        "type='" + type + '\'' +
                        ", room='" + room + '\'' +
                        ", productname='" + productname + '\'' +
                        ", sn='" + sn + '\'' +
                        ", way='" + way + '\'' +
                        ", eleState='" + eleState + '\'' +
                        ", onlinestate='" + onlinestate + '\'' +
                        ", name='" + name + '\'' +
                        ", state='" + state + '\'' +
                        ", value='" + value + '\'' +
                        ", paramtype='" + paramtype + '\'' +
                        ", condition='" + condition + '\'' +
                        ", clicksenceid='" + clicksenceid + '\'' +
                        ", num='" + num + '\'' +
                        ", type1='" + type1 + '\'' +
                        ", time='" + time + '\'' +
                        ", key='" + key + '\'' +
                        ", luminance='" + luminance + '\'' +
                        ", delaytime='" + delaytime + '\'' +
                        ", isChecked=" + isChecked +
                        ", panelkeys=" + panelkeys +
                        '}';
            }

            public String getDelaytime() {
                return delaytime;
            }

            public void setDelaytime(String delaytime) {
                this.delaytime = delaytime;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getLuminance() {
                return luminance;
            }

            public void setLuminance(String luminance) {
                this.luminance = luminance;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getParamtype() {
                return paramtype;
            }

            public void setParamtype(String paramtype) {
                this.paramtype = paramtype;
            }

            public String getCondition() {
                return condition;
            }

            public void setCondition(String condition) {
                this.condition = condition;
            }

            public String getClicksenceid() {
                return clicksenceid;
            }

            public void setClicksenceid(String clicksenceid) {
                this.clicksenceid = clicksenceid;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getType1() {
                return type1;
            }

            public void setType1(String type1) {
                this.type1 = type1;
            }

            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean isChecked) {
                this.isChecked = isChecked;
            }

            public String getOnlinestate() {
                return onlinestate;
            }

            public void setOnlinestate(String onlinestate) {
                this.onlinestate = onlinestate;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRoom() {
                return room;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public String getProductname() {
                return productname;
            }

            public void setProductname(String productname) {
                this.productname = productname;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getWay() {
                return way;
            }

            public void setWay(String way) {
                this.way = way;
            }

            public String getEleState() {
                return eleState;
            }

            public void setEleState(String eleState) {
                this.eleState = eleState;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            private List<Panelkeys> panelkeys;

            public List<Panelkeys> getPanelLists() {
                return panelkeys;
            }

            public void setPanelLists(List<Panelkeys> panelkeys) {
                this.panelkeys = panelkeys;
            }

            public static class Panelkeys implements Serializable {
                private String way;
                private String name;

                private List<Sences> sences;

                public String getWay() {
                    return way;
                }

                public void setWay(String way) {
                    this.way = way;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<Sences> getSences() {
                    return sences;
                }

                public void setSences(List<Sences> sences) {
                    this.sences = sences;
                }

                public static class Sences implements Serializable {

                    private String status;
                    private String sence_id;

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getSence_id() {
                        return sence_id;
                    }

                    public void setSence_id(String sence_id) {
                        this.sence_id = sence_id;
                    }

                    @Override
                    public String toString() {
                        return "Sences{" +
                                "status='" + status + '\'' +
                                ", sence_id='" + sence_id + '\'' +
                                '}';
                    }
                }

                @Override
                public String toString() {
                    return "Panelkeys{" +
                            "way='" + way + '\'' +
                            ", name='" + name + '\'' +
                            ", sences=" + sences +
                            '}';
                }
            }
        }
    }
}
