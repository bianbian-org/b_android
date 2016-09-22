package com.techjumper.polyhomeb.entity.medicalEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalMainEntity {


    /**
     * data : [{"id":4361,"pid":"100000001617","pname":"陆浩","measureTime":"2016-08-15 13:37","recvTime":"2016-08-15 13:37","status":0,"remark":"","ahdId":"0C:82:68:A7:82:3E","deviceId":null,"measureType":null,"result":"2,1,4,1,1,3,2,3","comments":"体重正常,检测出来的身体年龄比实际年龄轻,肌肉率高,脂肪率偏低,内脏脂肪含量正常,基础代谢偏高,身体骨骼含量标准,身体水分含量偏高,","mdevice":"新瑞时身体成分仪C系列","weight":58.6,"height":177,"bmi":18.7,"bfr":6,"vfl":2,"bwr":68.8,"bme":1803,"smrwb":33.1,"skms":3.8,"wtResult":"2","bfrResult":"1","vflResult":"1","smrResult":"4","baResult":"1","bmeResult":"3","bwrResult":"3","skmsResult":"2","impedance":-1,"bodyType":-1,"bodyAge":22}]
     * count : 67
     */

    private int count;
    /**
     * id : 4361
     * pid : 100000001617
     * pname : 陆浩
     * measureTime : 2016-08-15 13:37
     * recvTime : 2016-08-15 13:37
     * status : 0
     * remark :
     * ahdId : 0C:82:68:A7:82:3E
     * deviceId : null
     * measureType : null
     * result : 2,1,4,1,1,3,2,3
     * comments : 体重正常,检测出来的身体年龄比实际年龄轻,肌肉率高,脂肪率偏低,内脏脂肪含量正常,基础代谢偏高,身体骨骼含量标准,身体水分含量偏高,
     * mdevice : 新瑞时身体成分仪C系列
     * weight : 58.6
     * height : 177.0
     * bmi : 18.7
     * bfr : 6.0
     * vfl : 2.0
     * bwr : 68.8
     * bme : 1803.0
     * smrwb : 33.1
     * skms : 3.8
     * wtResult : 2
     * bfrResult : 1
     * vflResult : 1
     * smrResult : 4
     * baResult : 1
     * bmeResult : 3
     * bwrResult : 3
     * skmsResult : 2
     * impedance : -1
     * bodyType : -1
     * bodyAge : 22
     */

    private List<DataBean> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String pid;
        private String pname;
        private String measureTime;
        private String recvTime;
        private int status;
        private int systolic;
        private int diastolic;
        private int heartRate;
        private int stepCount;
        private String remark;
        private String ahdId;
        private Object deviceId;
        private Object measureType;
        private String result;
        private String comments;
        private String mdevice;
        private double bgValue;
        private double spo2;
        private double weight;
        private double height;
        private double bmi;
        private double bfr;
        private double vfl;
        private double bwr;
        private double bme;
        private double smrwb;
        private double skms;
        private double btValue;
        private String wtResult;
        private String bfrResult;
        private String vflResult;
        private String smrResult;
        private String baResult;
        private String bmeResult;
        private String bwrResult;
        private String skmsResult;
        private int impedance;
        private int bodyType;
        private int bodyAge;

        public double getBtValue() {
            return btValue;
        }

        public void setBtValue(double btValue) {
            this.btValue = btValue;
        }

        public int getStepCount() {
            return stepCount;
        }

        public void setStepCount(int stepCount) {
            this.stepCount = stepCount;
        }

        public double getSpo2() {
            return spo2;
        }

        public void setSpo2(double spo2) {
            this.spo2 = spo2;
        }

        public double getBgValue() {
            return bgValue;
        }

        public void setBgValue(double bgValue) {
            this.bgValue = bgValue;
        }

        public int getSystolic() {
            return systolic;
        }

        public void setSystolic(int systolic) {
            this.systolic = systolic;
        }

        public int getDiastolic() {
            return diastolic;
        }

        public void setDiastolic(int diastolic) {
            this.diastolic = diastolic;
        }

        public int getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(String measureTime) {
            this.measureTime = measureTime;
        }

        public String getRecvTime() {
            return recvTime;
        }

        public void setRecvTime(String recvTime) {
            this.recvTime = recvTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAhdId() {
            return ahdId;
        }

        public void setAhdId(String ahdId) {
            this.ahdId = ahdId;
        }

        public Object getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(Object deviceId) {
            this.deviceId = deviceId;
        }

        public Object getMeasureType() {
            return measureType;
        }

        public void setMeasureType(Object measureType) {
            this.measureType = measureType;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getMdevice() {
            return mdevice;
        }

        public void setMdevice(String mdevice) {
            this.mdevice = mdevice;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public double getBfr() {
            return bfr;
        }

        public void setBfr(double bfr) {
            this.bfr = bfr;
        }

        public double getVfl() {
            return vfl;
        }

        public void setVfl(double vfl) {
            this.vfl = vfl;
        }

        public double getBwr() {
            return bwr;
        }

        public void setBwr(double bwr) {
            this.bwr = bwr;
        }

        public double getBme() {
            return bme;
        }

        public void setBme(double bme) {
            this.bme = bme;
        }

        public double getSmrwb() {
            return smrwb;
        }

        public void setSmrwb(double smrwb) {
            this.smrwb = smrwb;
        }

        public double getSkms() {
            return skms;
        }

        public void setSkms(double skms) {
            this.skms = skms;
        }

        public String getWtResult() {
            return wtResult;
        }

        public void setWtResult(String wtResult) {
            this.wtResult = wtResult;
        }

        public String getBfrResult() {
            return bfrResult;
        }

        public void setBfrResult(String bfrResult) {
            this.bfrResult = bfrResult;
        }

        public String getVflResult() {
            return vflResult;
        }

        public void setVflResult(String vflResult) {
            this.vflResult = vflResult;
        }

        public String getSmrResult() {
            return smrResult;
        }

        public void setSmrResult(String smrResult) {
            this.smrResult = smrResult;
        }

        public String getBaResult() {
            return baResult;
        }

        public void setBaResult(String baResult) {
            this.baResult = baResult;
        }

        public String getBmeResult() {
            return bmeResult;
        }

        public void setBmeResult(String bmeResult) {
            this.bmeResult = bmeResult;
        }

        public String getBwrResult() {
            return bwrResult;
        }

        public void setBwrResult(String bwrResult) {
            this.bwrResult = bwrResult;
        }

        public String getSkmsResult() {
            return skmsResult;
        }

        public void setSkmsResult(String skmsResult) {
            this.skmsResult = skmsResult;
        }

        public int getImpedance() {
            return impedance;
        }

        public void setImpedance(int impedance) {
            this.impedance = impedance;
        }

        public int getBodyType() {
            return bodyType;
        }

        public void setBodyType(int bodyType) {
            this.bodyType = bodyType;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(int bodyAge) {
            this.bodyAge = bodyAge;
        }


    }
}
