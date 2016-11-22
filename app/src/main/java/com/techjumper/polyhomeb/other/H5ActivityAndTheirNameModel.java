package com.techjumper.polyhomeb.other;

import android.app.Activity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class H5ActivityAndTheirNameModel {

    private String name;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public H5ActivityAndTheirNameModel(String name, Activity activity) {

        this.name = name;
        this.activity = activity;
    }
}
