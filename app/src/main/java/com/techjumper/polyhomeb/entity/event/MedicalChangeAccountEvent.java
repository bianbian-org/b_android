package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangeAccountEvent {

    private String name;

    public MedicalChangeAccountEvent(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
