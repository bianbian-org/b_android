package com.techjumper.commonres.entity;

import com.techjumper.plugincommunicateengine.entity.core.BaseMessageEntity;

import java.util.List;

/**
 * 医疗信息
 * Created by kevin on 16/7/7.
 */

public class MedicalEntity extends BaseMessageEntity<MedicalEntity.MedicalDataEntity> {
    public static class MedicalDataEntity {
        private String name;
        private MedicalData values;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MedicalData getValues() {
            return values;
        }

        public void setValues(MedicalData values) {
            this.values = values;
        }
    }

    public static class MedicalItemEntity {

        private String bgValue;
        private String bpValue;
        private String heartRate;
        private String name;

        public String getBgValue() {
            return bgValue;
        }

        public void setBgValue(String bgValue) {
            this.bgValue = bgValue;
        }

        public String getBpValue() {
            return bpValue;
        }

        public void setBpValue(String bpValue) {
            this.bpValue = bpValue;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(String heartRate) {
            this.heartRate = heartRate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class MedicalData {
        private String Datas;

        public String getDatas() {
            return Datas;
        }

        public void setDatas(String datas) {
            Datas = datas;
        }
    }
}
