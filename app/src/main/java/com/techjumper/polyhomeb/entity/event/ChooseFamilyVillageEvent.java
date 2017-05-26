package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseFamilyVillageEvent {

    private String name;
    private int isFamilyData;
    private int position;

    public ChooseFamilyVillageEvent(String name, int isFamilyData, int position) {
        this.name = name;
        this.isFamilyData = isFamilyData;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int isFamilyData() {
        return isFamilyData;
    }

    public int getPosition() {
        return position;
    }
}
