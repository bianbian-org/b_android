package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseVillageFamilyData {

    private String name;
    private boolean isChoosed;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}