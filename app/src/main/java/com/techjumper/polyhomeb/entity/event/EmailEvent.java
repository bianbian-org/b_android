package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class EmailEvent {
    private String email;

    public EmailEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
