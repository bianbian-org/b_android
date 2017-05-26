package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseVillageEvent {

    private String name;

    public ChooseVillageEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
