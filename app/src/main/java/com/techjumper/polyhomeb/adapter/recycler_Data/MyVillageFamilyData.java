package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MyVillageFamilyData {

    private int family_id;
    private boolean choosed;
    private String name;
    private int verified;
    private int isFamilyData;  //0是家庭  1是小区
    private int villageId;

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public int isFamilyData() {
        return isFamilyData;
    }

    public void setFamilyData(int familyData) {
        isFamilyData = familyData;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getFamily_id() {
        return family_id;
    }

    public void setFamily_id(int family_id) {
        this.family_id = family_id;
    }
}
