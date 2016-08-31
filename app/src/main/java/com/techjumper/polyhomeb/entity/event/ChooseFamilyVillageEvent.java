package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseFamilyVillageEvent {

    private int id;
    private String name;
    private int isFamilyData;
    private int position;

    public ChooseFamilyVillageEvent(int id, String name, int isFamilyData, int position) {
        this.id = id;
        this.name = name;
        this.isFamilyData = isFamilyData;
        this.position = position;
    }

    public int getId() {
        return id;
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
